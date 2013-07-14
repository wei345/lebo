package com.lebo.service;

import com.google.common.collect.Lists;
import com.lebo.entity.Following;
import com.lebo.entity.Post;
import com.lebo.repository.FollowingDao;
import com.lebo.repository.PostDao;
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
    private GridFsService gridFsService;
    @Autowired
    private MentionService mentionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private FavoriteService favoriteService;

    /**
     *
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
            for(String fileId : fileIds){
                gridFsService.delete(fileId);
            }
            throw e;
        }

        Post post = new Post();
        post.setUserId(userId);
        post.setCreatedAt(new Date());
        post.setText(text);
        post.setMentions(mentionService.mentionUserIds(text));
        post.setTags(mentionService.findTags(text));
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

    public List<StatusDto> toStatusDtoList(List<Post> posts){
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

    public List<Post> searchPosts(SearchParam param){
        return postDao.search(param.getQ(), param.getMaxId(), param.getSinceId(), param).getContent();
    }

    public List<Tag> searchTags(String q, int count){
        List<Tag> allTags = findAllTags();
        List<Tag> result = new ArrayList<Tag>();

        for(Tag tag : allTags){
            if(tag.getName().contains(q)){
                result.add(tag);
                if(result.size() == count){
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
    private void onPostCreated(){
        //发送JMS消息
        //更新Tags统计数据
    }

    /**
     * 返回所有Tag，按次数由大到小排序。
     */
    public List<Tag> findAllTags(){
        //TODO 优化findAllTags，读写通过缓存
        //查最近3个月？
        //返回结果带有最后出现日期？

        String map = "function(){for(var i in this.tags) emit(this.tags[i], 1)}";
        String reduce = "function(key, emits){total = 0; for(var i in emits) total += emits[i]; return total;}";
        MapReduceResults<Tag> result =  mongoTemplate.mapReduce(mongoTemplate.getCollectionName(Post.class), map, reduce, Tag.class);

        List<Tag> tags = Lists.newArrayList(result.iterator());
        Collections.sort(tags);

        return tags;
    }

    public static class Tag implements Comparable<Tag>{
        private String _id;
        private Integer value;

        public String getName() {
            return _id;
        }

        public Integer getCount(){
            return value;
        }

        /**
         * //按value由大到小排序
         */
        @Override
        public int compareTo(Tag o) {
            return o.value.compareTo(value);
        }
    }
}
