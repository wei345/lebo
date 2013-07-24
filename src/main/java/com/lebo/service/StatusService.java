package com.lebo.service;

import com.google.common.collect.Lists;
import com.lebo.entity.Following;
import com.lebo.entity.Post;
import com.lebo.entity.User;
import com.lebo.repository.FollowingDao;
import com.lebo.repository.MongoConstant;
import com.lebo.repository.PostDao;
import com.lebo.repository.UserDao;
import com.lebo.rest.dto.StatusDto;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.FileInfo;
import com.lebo.service.param.PaginationParam;
import com.lebo.service.param.StatusFilterParam;
import com.lebo.service.param.TimelineParam;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
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
    private AccountService accountService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private Segmentation segmentation;
    @Autowired
    private CommentService commentService;


    /**
     * @param userId
     * @param text
     * @param fileInfos
     * @param originPostId 若是转发，为原Post ID，否则为null
     * @return
     * @throws IOException
     */
    public Post update(String userId, String text, List<FileInfo> fileInfos, String originPostId, String source) throws Exception {
        List<String> fileIds = gridFsService.saveFilesSafely(fileInfos);

        Post post = Post.initial(new Post());
        post.setUserId(userId);
        post.setCreatedAt(new Date());
        post.setSource(source);
        post.setText(text);
        post.setUserMentions(mentionUserIds(text));
        post.setHashtags(findHashtags(text));
        post.setFiles(fileIds);
        post.setOriginPostId(originPostId);
        post.setSearchTerms(buildSearchTerms(post));

        post = postDao.save(post);
        throwOnMongoError();
        onPostCreated();

        return post;
    }

    public boolean isRepost(String userId, String postId) {
        return postDao.findByUserIdAndOriginPostId(userId, postId) != null;
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
        // 一次取出所有follows？如果数量很多怎么办？少峰 2013.07.18
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

    /**
     * hot列表
     *
     * @param param
     * @return
     */
    public List<Post> hotTimeline(TimelineParam param) {
        Assert.hasText(param.getUserId(), "The userId can not be null");
        // 一次取出所有follows？如果数量很多怎么办？少峰 2013.07.18
        List<Following> followingList = followingDao.findByUserId(param.getUserId());

        List<String> followingIdList = new ArrayList<String>(followingList.size() + 1);
        for (Following following : followingList) {
            followingIdList.add(following.getFollowingId());
        }

        followingIdList.add(param.getUserId());

        return postDao.homeTimeline(followingIdList, param.getMaxId(), param.getSinceId(), param).getContent();
    }

    public Post findPost(String id) {
        return postDao.findOne(id);
    }

    public List<Post> findPosts(List<String> ids) {
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        for (int i = 0; i < ids.size(); i++) {
            objectIds.add(new ObjectId(ids.get(i)));
        }

        return postDao.findPosts(objectIds);
    }

    public int countUserStatus(String userId) {
        return (int) mongoTemplate.count(new Query(new Criteria(Post.USER_ID_KEY).is(userId)), Post.class);
    }

    public StatusDto toStatusDto(Post post) {
        StatusDto dto = BeanMapper.map(post, StatusDto.class);
        dto.setUser(accountService.toUserDto(accountService.getUser(post.getUserId())));

        //该Post为原始Post，非转发
        if (post.getOriginPostId() == null) {
            dto.setRepostsCount(countReposts(post.getId()));
            dto.setReposted(isReposted(accountService.getCurrentUserId(), post.getId()));
            dto.setFavorited(favoriteService.isFavorited(accountService.getCurrentUserId(), post.getId()));
            dto.setCommentsCount(commentService.countPostComments(post.getId()));

            List<StatusDto.FileInfoDto> fileInfoDtos = new ArrayList<StatusDto.FileInfoDto>(2);
            for (String fileId : post.getFiles()) {
                fileInfoDtos.add(gridFsService.getFileInfoDto(fileId, "?postId=" + post.getId()));
            }
            dto.setFiles(fileInfoDtos);
        }
        // 嵌入转发的POST
        else {
            Post originPost = postDao.findOne(post.getOriginPostId());
            StatusDto originStatusDto = toStatusDto(originPost);
            dto.setOriginStatus(originStatusDto);
        }

        return dto;
    }

    public String getOriginPostId(Post post) {
        return post.getOriginPostId() == null ? post.getId() : post.getOriginPostId();
    }

    public boolean isOriginPost(Post post) {
        return post.getOriginPostId() == null;
    }

    public int countReposts(String postId) {
        return (int) mongoTemplate.count(new Query(new Criteria(Post.ORIGIN_POST_ID_KEY).is(postId)), Post.class);
    }

    public List<StatusDto> toStatusDtoList(List<Post> posts) {
        List<StatusDto> dtoList = Lists.newArrayList();
        for (Post post : posts) {
            dtoList.add(toStatusDto(post));
        }
        return dtoList;
    }

    public List<Post> searchPosts(StatusFilterParam param) {
        List<Criteria> criteriaList = new ArrayList<Criteria>(5);

        if (StringUtils.isNotBlank(param.getFollow())) {
            String[] userIds = param.getFollow().split("\\s*,\\s*");
            //用户ID之间or关系
            criteriaList.add(new Criteria(Post.USER_ID_KEY).in(Arrays.asList(userIds)));
        }

        if (StringUtils.isNotBlank(param.getTrack())) {
            String[] phrases = param.getTrack().split("\\s*,\\s*");
            List<Criteria> criterias = new ArrayList<Criteria>(phrases.length);
            for (String phrase : phrases) {
                String[] keywords = phrase.split("\\s+");
                List<String> keywordList = new ArrayList<String>(keywords.length);
                for (String keyword : keywords) {
                    keywordList.add(
                            isHashtagOrAtSomeone(keyword) ? keyword : keyword.toLowerCase());
                }
                //短语中关键词之间是and关系
                criterias.add(new Criteria(Post.SEARCH_TERMS_KEY).all(keywordList));
            }
            //短语之间是or关系
            if (criterias.size() > 1) {
                criteriaList.add(orOperator(criterias));
            } else if (criterias.size() == 1) {
                criteriaList.add(criterias.get(0));
            }
        }

        if (param.getAfter() != null) {
            criteriaList.add(new Criteria(Post.CREATED_AT_KEY).gte(param.getAfter()));
        }

        //分页
        if (!param.getMaxId().equals(MongoConstant.MONGO_ID_MAX_VALUE)) {
            criteriaList.add(new Criteria("_id").lt(new ObjectId(param.getMaxId())));
        }
        if (!param.getSinceId().equals(MongoConstant.MONGO_ID_MIN_VALUE)) {
            criteriaList.add(new Criteria("_id").gt(new ObjectId(param.getSinceId())));
        }

        Criteria queryCriteria = null;
        if (criteriaList.size() > 1) {
            //各条件间是and关系
            queryCriteria = andOperator(criteriaList);
        } else if (criteriaList.size() == 1) {
            queryCriteria = criteriaList.get(0);
        }

        Query query = new Query();
        if (queryCriteria != null) {
            query.addCriteria(queryCriteria);
        }
        query.with(PaginationParam.DEFAULT_SORT).limit(param.getCount());

        return mongoTemplate.find(query, Post.class);
    }

    public List<Hashtag> searchHashtags(String q, int count) {
        List<Hashtag> allHashtags = findAllHashtags();
        List<Hashtag> matchedHashtags = new ArrayList<Hashtag>();

        for (Hashtag hashtag : allHashtags) {
            if (hashtag.getName().contains(q)) {
                matchedHashtags.add(hashtag);
                if (matchedHashtags.size() == count) {
                    break;
                }
            }
        }

        return matchedHashtags;
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

    private Pattern mentionPattern = Pattern.compile("@([^@#\\s]+)");

    public LinkedHashSet<String> mentionScreenNames(String text, boolean trimAt) {
        if(StringUtils.isBlank(text)){
            return new LinkedHashSet<String>(1);
        }
        Matcher m = mentionPattern.matcher(text);
        LinkedHashSet<String> names = new LinkedHashSet<String>();
        while (m.find()) {
            names.add(trimAt ? m.group(1) : m.group(0));
        }
        return names;
    }

    public LinkedHashSet<String> mentionUserIds(String text) {
        LinkedHashSet<String> userIds = new LinkedHashSet<String>();

        LinkedHashSet<String> names = mentionScreenNames(text, true);
        for (String screenName : names) {
            User user = userDao.findByScreenName(screenName);
            if (user != null) {
                userIds.add(user.getId());
            }
        }
        return userIds;
    }

    private Pattern tagPattern = Pattern.compile("#([^#@\\s]+)#");

    public LinkedHashSet<String> findHashtags(String text) {
        if(StringUtils.isBlank(text)){
            return new LinkedHashSet<String>(1);
        }
        Matcher m = tagPattern.matcher(text);
        LinkedHashSet<String> tags = new LinkedHashSet<String>();
        while (m.find()) {
            tags.add(m.group(0));
        }
        return tags;
    }

    private Pattern hashtagOrAtSomeonePattern = Pattern.compile("^#([^#@\\s]+)#|@([^@#\\s]+)$");

    public boolean isHashtagOrAtSomeone(String text) {
        return hashtagOrAtSomeonePattern.matcher(text).matches();
    }

    public LinkedHashSet<String> buildSearchTerms(Post post) {
        LinkedHashSet<String> words = new LinkedHashSet<String>();
        words.addAll(findHashtags(post.getText()));
        words.addAll(mentionScreenNames(post.getText(), false));
        words.addAll(segmentation.findWords(post.getText()));
        return words;
    }

    public boolean isReposted(String userId, String postId) {
        return postDao.findByUserIdAndOriginPostId(userId, postId) != null;
    }

    //增长浏览数
    public void increaseViewsCount(String id) {
        mongoTemplate.updateFirst(new Query(new Criteria("_id").is(id)),
                new Update().inc(Post.VIEWS_COUNT_KEY, 1),
                Post.class);
    }

    public void increaseFavoritesCount(String id) {
        mongoTemplate.updateFirst(new Query(new Criteria("_id").is(id)),
                new Update().inc(Post.FAVOURITES_COUNT_KEY, 1),
                Post.class);
    }

    public void decreaseFavoritesCount(String id) {
        mongoTemplate.updateFirst(new Query(new Criteria("_id").is(id)),
                new Update().inc(Post.FAVOURITES_COUNT_KEY, -1),
                Post.class);
    }
}
