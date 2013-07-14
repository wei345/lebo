package com.lebo.service;

import com.google.common.collect.Lists;
import com.lebo.entity.Following;
import com.lebo.entity.Post;
import com.lebo.entity.User;
import com.lebo.repository.FollowingDao;
import com.lebo.repository.PostDao;
import com.lebo.repository.UserDao;
import com.lebo.rest.dto.StatusDto;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.FileInfo;
import com.lebo.service.param.SearchParam;
import com.lebo.service.param.TimelineParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springside.modules.mapper.BeanMapper;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Wei Liu
 * Date: 13-7-2
 * Time: PM4:32
 */
@Service
public class StatusService extends AbstractMongoService {
    public static final int MAX_TEXT_LENGTH = 140;

    @Autowired
    private FollowingDao followingDao;
    @Autowired
    private PostDao postDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GridFsService gridFsService;
    @Autowired
    private MentionService mentionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private FavoriteService favoriteService;

    /**
     * @param userId
     * @param text
     * @param fileInfos
     * @param originPostId 若是转发，为原Post ID，否则为null
     * @return
     * @throws IOException
     */
    public Post update(String userId, String text, List<FileInfo> fileInfos, String originPostId, String source) throws Exception {
        List<String> fileIds = Lists.newArrayList();
        try {
            for (FileInfo fileInfo : fileInfos) {
                fileIds.add(gridFsService.save(fileInfo.getContent(), fileInfo.getFilename(), fileInfo.getMimeType()));
            }
        } catch (Exception e) {
            for (String fileId : fileIds) {
                gridFsService.delete(fileId);
            }
            throw e;
        }

        Post post = new Post();
        post.setUserId(userId);
        post.setCreatedAt(new Date());
        post.setText(text);
        post.setUserMentions(mentionUserIds(text));
        post.setHashtags(findHashtags(text));
        post.setFiles(fileIds);
        post.setOriginPostId(originPostId);
        post.setSource(source);

        post = postDao.save(post);
        throwOnMongoError();
        onPostCreated();

        return post;
    }

    public List<Post> userTimeline(TimelineParam param) {
        Assert.hasText(param.getUserId(), "The userId can not be null");

        if (param.canIgnoreIdCondition()) {
            return postDao.findByUserId(param.getUserId(), param).getContent();
        } else {
            return postDao.userTimeline(param.getUserId(), param.getMaxId(), param.getSinceId(), param).getContent();
        }
    }

    public List<Post> homeTimeline(TimelineParam param) {
        Assert.hasText(param.getUserId(), "The userId can not be null");
        List<Following> followingList = followingDao.findByUserId(param.getUserId());

        List<String> followingIdList = new ArrayList<String>(followingList.size() + 1);
        for (Following following : followingList) {
            followingIdList.add(following.getFollowingId());
        }

        followingIdList.add(param.getUserId());

        return postDao.homeTimeline(followingIdList, param.getMaxId(), param.getSinceId(), param).getContent();
    }

    public List<Post> mentionsTimeline(TimelineParam param) {
        Assert.hasText(param.getUserId(), "The userId can not be null");
        return postDao.mentionsTimeline(param.getUserId(), param.getMaxId(), param.getSinceId(), param).getContent();
    }

    public Post findPost(String id) {
        return postDao.findOne(id);
    }

    public int countUserStatus(String userId) {
        return (int) mongoTemplate.count(new Query(new Criteria(Post.POST_USER_ID_KEY).is(userId)), Post.class);
    }

    public StatusDto toStatusDto(Post post) {
        String userId = accountService.getCurrentUserId();
        StatusDto dto = BeanMapper.map(post, StatusDto.class);

        // 嵌入转发的POST
        if (post.getOriginPostId() != null) {
            Post originPost = postDao.findOne(post.getOriginPostId());
            StatusDto originStatusDto = toStatusDto(originPost);
            dto.setOriginStatus(originStatusDto);
        }

        dto.setUser(accountService.toUserDto(accountService.getUser(post.getUserId())));
        dto.setFavorited(favoriteService.isFavorited(userId, post.getId()));
        dto.setFavouritesCount(favoriteService.countPostFavorites(post.getId()));

        return dto;
    }

    public List<StatusDto> toStatusDtoList(List<Post> posts) {
        List<StatusDto> dtoList = Lists.newArrayList();
        for (Post post : posts) {
            dtoList.add(toStatusDto(post));
        }
        return dtoList;
    }

    public void increaseRepostsCount(String postId) {
        mongoTemplate.updateFirst(new Query(new Criteria("_id").is(postId)),
                new Update().inc(Post.REPOSTS_COUNT_KEY, 1),
                Post.class);
    }

    public List<Post> searchPosts(SearchParam param) {
        return postDao.search(param.getQ(), param.getMaxId(), param.getSinceId(), param).getContent();
    }

    public List<Hashtag> searchHashtags(String q, int count) {
        List<Hashtag> allHashtags = findAllHashtags();
        List<Hashtag> result = new ArrayList<Hashtag>();

        for (Hashtag hashtag : allHashtags) {
            if (hashtag.getName().contains(q)) {
                result.add(hashtag);
                if (result.size() == count) {
                    break;
                }
            }
        }

        return result;
    }


    /**
     * 创建了Post。可能需要统计Tags或更新全文索引。
     */
    // TODO 完成 onPostCreated
    private void onPostCreated() {
        //发送JMS消息
        //更新Tags统计数据
    }

    /**
     * 返回所有Tag，按次数由大到小排序。
     */
    public List<Hashtag> findAllHashtags() {
        //TODO 优化findAllTags，读写通过缓存
        //查最近3个月？
        //返回结果带有最后出现日期？

        String map = String.format("function(){for(var i in this.%s) emit(this.%s[i], 1)}", Post.HASHTAGS_KEY, Post.HASHTAGS_KEY);
        String reduce = "function(key, emits){total = 0; for(var i in emits) total += emits[i]; return total;}";
        MapReduceResults<Hashtag> result = mongoTemplate.mapReduce(mongoTemplate.getCollectionName(Post.class), map, reduce, Hashtag.class);

        List<Hashtag> hashtags = Lists.newArrayList(result.iterator());
        Collections.sort(hashtags);

        return hashtags;
    }

    public static class Hashtag implements Comparable<Hashtag> {
        private String _id;
        private Integer value;

        public String getName() {
            return _id;
        }

        public Integer getCount() {
            return value;
        }

        /**
         * //按value由大到小排序
         */
        @Override
        public int compareTo(Hashtag o) {
            return o.value.compareTo(value);
        }
    }

    private Pattern mentionPattern = Pattern.compile("@([^@\\s]+)");
    private Pattern tagPattern = Pattern.compile("#([^#\\s]+)#");

    public LinkedHashSet<String> mentionUserIds(String text) {
        LinkedHashSet<String> userIds = new LinkedHashSet<String>();

        LinkedHashSet<String> names = mentionScreenNames(text);
        for (String screenName : names) {
            User user = userDao.findByScreenName(screenName);
            if (user != null) {
                userIds.add(user.getId());
            }
        }
        return userIds;
    }

    LinkedHashSet<String> mentionScreenNames(String text) {
        Matcher m = mentionPattern.matcher(text);
        LinkedHashSet<String> names = new LinkedHashSet<String>();
        while (m.find()) {
            names.add(m.group(1));
        }
        return names;
    }

    public LinkedHashSet<String> findHashtags(String text) {
        Matcher m = tagPattern.matcher(text);
        LinkedHashSet<String> tags = new LinkedHashSet<String>();
        while (m.find()) {
            tags.add(m.group(1));
        }
        return tags;
    }
}
