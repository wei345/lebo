package com.lebo.service;

import com.google.common.collect.Lists;
import com.lebo.Constants;
import com.lebo.entity.*;
import com.lebo.event.AfterPostCreateEvent;
import com.lebo.event.AfterPostDestroyEvent;
import com.lebo.event.ApplicationEventBus;
import com.lebo.event.BeforePostCreateEvent;
import com.lebo.repository.MongoConstant;
import com.lebo.repository.PostDao;
import com.lebo.repository.UserDao;
import com.lebo.rest.dto.StatusDto;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.*;
import com.mongodb.DBCollection;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Wei Liu
 * Date: 13-7-2
 * Time: PM4:32
 */
@ManagedResource(objectName = "lebo:name=StatusService", description = "Status Service Management Bean")
@Service
public class StatusService extends AbstractMongoService {
    public static final int MAX_TEXT_LENGTH = 140;

    private Logger logger = LoggerFactory.getLogger(StatusService.class);

    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private PostDao postDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private Segmentation segmentation;
    @Autowired
    private CommentService commentService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private ApplicationEventBus eventBus;
    public static final String FILE_COLLECTION_NAME = "post";

    /**
     * @param userId
     * @param text
     * @param video
     * @param videoFirstFrame 视频第一帧
     * @param originPost      若是转发，为原Post ID，否则为null
     * @param acl
     * @return
     * @throws IOException
     */
    public Post createPost(String userId, String text, FileInfo video, FileInfo videoFirstFrame, Post originPost, String source, Integer acl) {
        return createPost(userId, text, video, videoFirstFrame, originPost, source, acl, new Date());
    }

    public Post createPost(String userId, String text, FileInfo video, FileInfo videoFirstFrame, Post originPost, String source, Integer acl, Date createdAt) {
        Post post = new Post();

        post.initialCounts();
        post.setUserId(userId);
        post.setCreatedAt(createdAt == null ? new Date() : createdAt);
        post.setId(newMongoId(post.getCreatedAt()));
        post.setSource(source);
        post.setText(text);
        post.setUserMentions(findUserMentions(text));
        post.setMentionUserIds(mentionUserIds(post.getUserMentions()));
        post.setHashtags(findHashtags(text, true));
        post.setSearchTerms(buildSearchTerms(post));
        post.setAcl(acl);

        //原始贴
        if (originPost == null) {
            video.setKey(generateFileId(FILE_COLLECTION_NAME, post.getId(), "video", video.getLength(), video.getContentType(), video.getFilename()));
            videoFirstFrame.setKey(generateFileId(FILE_COLLECTION_NAME, post.getId(), "video-first-frame", videoFirstFrame.getLength(), videoFirstFrame.getContentType(), videoFirstFrame.getFilename()));

            fileStorageService.save(video, videoFirstFrame);

            post.setVideo(video);
            post.setVideoFirstFrame(videoFirstFrame);
        }
        //转发贴
        else {
            post.setOriginPostId(originPost.getId());
            post.setOriginPostUserId(originPost.getUserId());
        }

        eventBus.post(new BeforePostCreateEvent(post));
        post = postDao.save(post);
        throwOnMongoError();
        eventBus.post(new AfterPostCreateEvent(post));

        return post;
    }

    public Post destroyPost(String id) {
        Post post = postDao.findOne(id);
        if (post != null) {
            //原始帖子，删除评论、收藏、转发
            if (post.getOriginPostId() == null) {
                commentService.deleteByPostId(id);
                favoriteService.deleteByPostId(id);
                deleteReposts(id); //Twitter也会删除转发贴
            }

            if (post.getVideo() != null) {
                fileStorageService.delete(post.getVideo().getKey());
            }
            if (post.getVideoFirstFrame() != null) {
                fileStorageService.delete(post.getVideoFirstFrame().getKey());
            }
            if (post.getVideoConverted() != null) {
                fileStorageService.delete(post.getVideoConverted().getKey());
            }

            postDao.delete(post);
            eventBus.post(new AfterPostDestroyEvent(post));
        }
        return post;
    }

    private void deleteReposts(String originPostId) {
        mongoTemplate.remove(new Query(new Criteria(Post.ORIGIN_POST_ID_KEY).is(originPostId)), Post.class);
    }

    private void addAclPublicCriteria(Query query) {

        Criteria aclCriteria = new Criteria(Post.ACL_KEY).is(Post.ACL_PUBLIC);

        query.addCriteria(aclCriteria);
    }

    public List<Post> userTimeline(TimelineParam param) {
        Assert.hasText(param.getUserId(), "userId不能为空");

        if (!param.isIncludeOriginPosts() && !param.isIncludeReposts()) {
            //既不含原始帖也不含转发贴，那就没内容了
            return Collections.EMPTY_LIST;
        }

        Query query = new Query(new Criteria(Post.USER_ID_KEY).is(param.getUserId()));

        if (!param.isIncludeReposts()) {
            query.addCriteria(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null));
        }

        if (!param.isIncludeOriginPosts()) {
            query.addCriteria(new Criteria(Post.ORIGIN_POST_ID_KEY).ne(null));
        }

        paginationById(query, param);

        try {
            if (!param.getUserId().equals(accountService.getCurrentUserId())) {
                addAclPublicCriteria(query);
            }
        } catch (UnknownAccountException e) {
            addAclPublicCriteria(query);
        }

        return mongoTemplate.find(query, Post.class);
    }

    public List<Post> homeTimeline(TimelineParam param) {

        Assert.hasText(param.getUserId(), "userId不能为空");

        //TODO 一次取出所有follows？如果数量很多怎么办？
        List<String> followingIdList = friendshipService.getAllFriends(param.getUserId());

        followingIdList.add(param.getUserId());

        Query query = new Query(new Criteria(Post.USER_ID_KEY).in(followingIdList));
        paginationById(query, param);
        addAclPublicCriteria(query);

        return mongoTemplate.find(query, Post.class);
    }

    public List<Post> mentionsTimeline(TimelineParam param) {
        Assert.hasText(param.getUserId(), "userId不能为空");

        Query query = new Query(new Criteria(Post.MENTION_USER_IDS).is(param.getUserId()));
        paginationById(query, param);
        addAclPublicCriteria(query);

        return mongoTemplate.find(query, Post.class);
    }

    public Post getPost(String id) {
        return postDao.findOne(id);
    }

    public boolean postExists(String id){
        return postDao.exists(id);
    }

    public List<Post> getPostsWithOrder(List<ObjectId> idList) {

        List<Post> postList1 = mongoTemplate.find(new Query(new Criteria(Post.ID_KEY).in(idList)), Post.class);

        Map<String, Post> id2post = new HashMap<String, Post>(postList1.size());

        for (Post post : postList1) {
            id2post.put(post.getId(), post);
        }

        List<Post> postList2 = new ArrayList<Post>(postList1.size());

        for (ObjectId id : idList) {
            Post post = id2post.get(id.toString());
            if (post != null) {
                postList2.add(post);
            }
        }

        return postList2;
    }

    public int countPost(String userId) {
        return countPost(userId, null, null);
    }

    public int countPost(String userId, Boolean isPublic, Boolean isOrigin) {

        Query query = new Query(new Criteria(Post.USER_ID_KEY).is(userId));

        if (isPublic != null) {
            if (isPublic) {
                query.addCriteria(new Criteria(Post.ACL_KEY).is(Post.ACL_PUBLIC));
            } else {
                query.addCriteria(new Criteria(Post.ACL_KEY).ne(Post.ACL_PUBLIC));
            }
        }

        if (isOrigin != null) {
            if (isOrigin) {
                query.addCriteria(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null));
            } else {
                query.addCriteria(new Criteria(Post.ORIGIN_POST_ID_KEY).ne(null));
            }
        }

        return (int) mongoTemplate.count(query, Post.class);
    }

    public StatusDto mapStatusDto(Post post) {
        StatusDto dto = new StatusDto();
        dto.setId(post.getId());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setText(post.getText());
        if (post.getVideo() != null) {
            dto.setVideo(post.getVideo().toDto());
        }
        if (post.getVideoConverted() != null) {
            dto.setVideoConverted(post.getVideoConverted().toDto());
        }
        dto.setVideoFirstFrameUrl(post.getVideoFirstFrameUrl());
        dto.setRepostsCount(post.getRepostsCount());
        dto.setCommentsCount(post.getCommentsCount());
        dto.setShareCount(post.getShareCount());
        dto.setSource(post.getSource());
        dto.setFavoritesCount(post.getFavoritesCount());
        dto.setViewCount(post.getViewCount());
        if (post.getUserMentions() != null) {
            dto.setUserMentions(Post.UserMention.toDtos(post.getUserMentions()));
        }
        dto.setDigest(post.getDigest());
        dto.setPvt(post.getPvt());
        dto.setLastCommentCreatedAt(post.getLastCommentCreatedAt());
        dto.setPopularity(post.getPopularity());
        return dto;
    }

    //不含Perspectival属性(和当前登录用户相关，如是否收藏)
    //Perspectival来自Twitter，见https://dev.twitter.com/discussions/1053
    public StatusDto toBasicStatusDto(Post post) {
        StatusDto dto = mapStatusDto(post);
        dto.setUser(accountService.toBasicUserDto(accountService.getUser(post.getUserId())));

        //转发贴
        if (post.getOriginPostId() != null) {
            Post originPost = postDao.findOne(post.getOriginPostId());
            dto.setOriginStatus(toBasicStatusDto(originPost));
        }

        return dto;
    }

    public List<StatusDto> toBasicStatusDtos(List<Post> posts) {
        List<StatusDto> dtos = new ArrayList<StatusDto>(posts.size());
        for (Post post : posts) {
            dtos.add(toBasicStatusDto(post));
        }
        return dtos;
    }

    public StatusDto toStatusDto(Post post) {
        StatusDto dto = mapStatusDto(post);
        dto.setUser(accountService.toBasicUserDto(accountService.getUser(post.getUserId())));
        accountService.dtoSetFollowing(dto.getUser());

        //原帖
        if (post.getOriginPostId() == null) {
            dto.setReposted(isReposted(accountService.getCurrentUserId(), post));
            dto.setFavorited(favoriteService.isFavorited(accountService.getCurrentUserId(), post.getId()));

            //前3条评论
            CommentListParam commentListParam = new CommentListParam();
            //TODO 评论数量可配置
            commentListParam.setCount(3);
            commentListParam.setPostId(post.getId());
            List<Comment> comments = commentService.list(commentListParam);
            dto.setComments(commentService.toBasicCommentDtos(comments));

            //是否被当前登录用户转发
            dto.setReposted(isReposted(accountService.getCurrentUserId(), post));
        }
        //转发贴
        else {
            Post originPost = postDao.findOne(post.getOriginPostId());
            dto.setOriginStatus(toStatusDto(originPost));
        }

        return dto;
    }

    public String getOriginPostId(Post post) {
        return post.getOriginPostId() == null ? post.getId() : post.getOriginPostId();
    }

    public int countReposts(String postId) {
        return (int) mongoTemplate.count(new Query(new Criteria(Post.ORIGIN_POST_ID_KEY).is(postId)), Post.class);
    }

    public List<StatusDto> toStatusDtos(List<Post> posts) {
        List<StatusDto> dtoList = Lists.newArrayList();
        for (Post post : posts) {
            dtoList.add(toStatusDto(post));
        }
        return dtoList;
    }

    /**
     * 按ID降序排序，按ID分页。
     */
    public List<Post> filterPosts(StatusFilterParam param) {
        List<Criteria> criteriaList = new ArrayList<Criteria>(5);

        //follow
        if (StringUtils.isNotBlank(param.getFollow())) {
            String[] userIds = param.getFollow().split(Constants.COMMA_SEPARATOR);
            //用户ID之间or关系
            criteriaList.add(new Criteria(Post.USER_ID_KEY).in(Arrays.asList(userIds)));
        }

        //track
        if (StringUtils.isNotBlank(param.getTrack())) {
            String[] phrases = param.getTrack().split(Constants.COMMA_SEPARATOR);
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

        //after
        if (param.getAfter() != null) {
            criteriaList.add(new Criteria(Post.CREATED_AT_KEY).gte(param.getAfter()));
        }

        //分页
        Query query = new Query();
        if (!param.getMaxId().equals(MongoConstant.MONGO_ID_MAX_VALUE)) {
            criteriaList.add(new Criteria("_id").lt(new ObjectId(param.getMaxId())));
        }
        if (!param.getSinceId().equals(MongoConstant.MONGO_ID_MIN_VALUE)) {
            criteriaList.add(new Criteria("_id").gt(new ObjectId(param.getSinceId())));
        }
        query.with(PaginationParam.ID_DESC_SORT).limit(param.getCount());

        //查询
        Criteria queryCriteria = null;
        if (criteriaList.size() > 1) {
            queryCriteria = andOperator(criteriaList);
        } else if (criteriaList.size() == 1) {
            queryCriteria = criteriaList.get(0);
        }

        if (queryCriteria != null) {
            query.addCriteria(queryCriteria);
        }

        addAclPublicCriteria(query);

        return mongoTemplate.find(query, Post.class);
    }

    public List<Post> searchPosts(SearchParam param) {
        Query query = new Query();

        if (StringUtils.isNotBlank(param.getQ())) {
            query.addCriteria(new Criteria(Post.SEARCH_TERMS_KEY).is(param.getQ()));
        }

        if (param.getAfter() != null) {
            query.addCriteria(new Criteria(Post.CREATED_AT_KEY).gte(param.getAfter()));
        }

        query.with(param);
        return mongoTemplate.find(query, Post.class);
    }

    private static final Sort hotPostsSort = new Sort(Sort.Direction.DESC, Post.FAVOURITES_COUNT_KEY);

    /* 为避免刷屏，每用户只可上榜2条，不能这么简单的查询了
    /**
     * 热门:最近2天的帖子按红心数降序排序
     */
    /*public List<StatusDto> hotPosts(Integer page, Integer size) {
        Date daysAgo = DateUtils.addDays(dateProvider.getDate(), settingService.getSetting().getHotDays() * -1);
        Query query = new Query(new Criteria(Post.CREATED_AT_KEY).gt(daysAgo));
        query.addCriteria(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null));
        query.with(new PageRequest(page, size, hotPostsSort));
        List<Post> posts = mongoTemplate.find(query, Post.class);
        return toStatusDtos(posts);
    }*/

    /**
     * 读取热门帖子
     */
    public List<Post> hotPosts(Integer page, Integer size) {
        List<HotPost> hotPosts = mongoTemplate.find(
                new Query().with(new PageRequest(page, size, new Sort(Sort.Direction.ASC, HotPost.ID_KEY))),
                HotPost.class);

        List<Post> posts = new ArrayList<Post>(hotPosts.size());
        for (HotPost hotPost : hotPosts) {
            posts.add(getPost(hotPost.getPostId()));
        }

        return posts;
    }

    /* 不使用这种算法
    /**
     * 刷新热门帖子列表:最近2天被收藏的帖子按2天内的收藏数排序
     public void refreshHotPosts() {
     Date daysAgo = DateUtils.addDays(dateProvider.getDate(), settingService.getSetting().getHotDays() * -1);
     String mapFunction = String.format("function(){ emit(this.%s, 1); }", Favorite.POST_ID_KEY);
     String reduceFunction = "function(key, emits){ var total = 0; for(var i = 0; i < emits.length; i++){ total += emits[i]; } return total; }";

     //因为不需要返回结果，所有不用mongoTemplate#mapReduce
     DBObject dbObject = new BasicDBObject();
     dbObject.put("mapreduce", mongoTemplate.getCollectionName(Favorite.class));
     dbObject.put("map", mapFunction);
     dbObject.put("reduce", reduceFunction);
     dbObject.put("out", mongoTemplate.getCollectionName(HotPost.class));
     dbObject.put("query", QueryBuilder.start().put(Favorite.CREATED_AT_KEY).greaterThan(daysAgo).get());
     dbObject.put("verbose", true);

     logger.debug("正在刷新热门帖子: {}", dbObject);

     CommandResult result = mongoTemplate.executeCommand(dbObject);
     result.throwOnError();

     logger.debug("完成刷新热门帖子，result: {}", result);
     }
     */

    private Pattern mentionPattern = Pattern.compile("@([^@#\\s]+)");

    public LinkedHashSet<String> mentionUserIds(List<Post.UserMention> userMentions) {
        LinkedHashSet<String> userIds = new LinkedHashSet<String>();
        for (Post.UserMention userMention : userMentions) {
            userIds.add(userMention.getUserId());
        }
        return userIds;
    }

    public List<Post.UserMention> findUserMentions(String text) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<Post.UserMention>(1);
        }

        List<Post.UserMention> userMentions = new ArrayList<Post.UserMention>(5);

        Matcher m = mentionPattern.matcher(text);
        while (m.find()) {
            Post.UserMention userMention = new Post.UserMention();
            userMention.setIndices(Arrays.asList(m.start(1), m.end(1)));
            userMention.setScreenName(m.group(1));
            User user = userDao.findByScreenName(userMention.getScreenName());
            if (user != null) {
                userMention.setUserId(user.getId());
                userMentions.add(userMention);
            }
        }
        return userMentions;
    }

    private Pattern tagPattern = Pattern.compile("#([^#@\\s]+)#");

    public LinkedHashSet<String> findHashtags(String text, boolean strip) {
        if (StringUtils.isBlank(text)) {
            return new LinkedHashSet<String>(1);
        }
        Matcher m = tagPattern.matcher(text);
        LinkedHashSet<String> tags = new LinkedHashSet<String>();
        while (m.find()) {
            if (strip) {
                tags.add(m.group(1));
            } else {
                tags.add(m.group(0));
            }

        }
        return tags;
    }

    private Pattern hashtagOrAtSomeonePattern = Pattern.compile("^#([^#@\\s]+)#|@([^@#\\s]+)$");

    public boolean isHashtagOrAtSomeone(String text) {
        return hashtagOrAtSomeonePattern.matcher(text).matches();
    }

    public LinkedHashSet<String> buildSearchTerms(Post post) {
        LinkedHashSet<String> words = new LinkedHashSet<String>();
        words.addAll(findHashtags(post.getText(), false));
        for (Post.UserMention userMention : post.getUserMentions()) {
            words.add("@" + userMention.getScreenName());
        }
        words.addAll(segmentation.findWords(post.getText()));
        return words;
    }

    /**
     * 指定用户是否转发了指定post.
     *
     * @param userId
     * @param post
     * @return
     */
    public boolean isReposted(String userId, Post post) {
        String id = (post.getOriginPostId() == null ? post.getId() : post.getOriginPostId());

        Query query = new Query();
        query.addCriteria(new Criteria(Post.USER_ID_KEY).is(userId));
        query.addCriteria(new Criteria(Post.ORIGIN_POST_ID_KEY).is(id));
        return mongoTemplate.count(query, Post.class) > 0;
    }

    public Post getRepost(String userId, Post post) {
        String id = (post.getOriginPostId() == null ? post.getId() : post.getOriginPostId());

        return getRepost(userId, id);
    }

    public Post getRepost(String userId, String postId) {
        Query query = new Query();
        query.addCriteria(new Criteria(Post.USER_ID_KEY).is(userId));
        query.addCriteria(new Criteria(Post.ORIGIN_POST_ID_KEY).is(postId));
        return mongoTemplate.findOne(query, Post.class);
    }

    /**
     * 增长浏览数
     */
    public void increaseViewCount(String id) {
        mongoTemplate.updateFirst(new Query(new Criteria("_id").is(id)),
                new Update().inc(Post.VIEW_COUNT_KEY, 1),
                Post.class);
    }

    /**
     * 批量增长浏览数
     */
    public void increaseViewCount(List<String> ids) {
        mongoTemplate.updateMulti(new Query(new Criteria("_id").in(ids)),
                new Update().inc(Post.VIEW_COUNT_KEY, 1),
                Post.class);
    }

    /**
     * 分享次数+1
     */
    public void increaseShareCount(String postId) {
        Query query = new Query(new Criteria(Post.ID_KEY).is(postId));
        query.addCriteria(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null)); //确保是原帖，不给转发贴计数

        mongoTemplate.updateFirst(query,
                new Update().inc(Post.SHARE_COUNT_KEY, 1),
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

    /**
     * 查找某频道的Posts，按ID降序排序，按ID分页。
     */
    public List<Post> getChannelPosts(String name, PaginationParam paginationParam) {

        return getChannelPosts(name, paginationParam, null);
    }

    public List<Post> getChannelPosts(String name, PageRequest pageRequest) {
        return getChannelPosts(name, null, pageRequest);
    }

    public List<Post> getChannelPosts(String name, PaginationParam paginationParam, PageRequest pageRequest) {

        Channel channel = settingService.getChannelByName(name);

        Criteria queryCriteria = parseChannelFollowAndTrack(name, channel);

        Query query = new Query(queryCriteria);

        addAclPublicCriteria(query);

        //按id分页
        if (paginationParam != null) {

            paginationById(query, paginationParam);

            //带有置顶视频
            if (channel != null
                    && StringUtils.isNotBlank(channel.getTopPostId())                         //该频道有置顶视频
                    && paginationParam.getMaxId().equals(MongoConstant.MONGO_ID_MAX_VALUE)) { //第一页

                return getChannelPostsWithTopPost(query, channel, paginationParam.getCount());
            }
        }
        //按pageNumber和pageSize分页
        else if (pageRequest != null) {

            query.with(pageRequest);

            //带有置顶视频
            if (channel != null
                    && StringUtils.isNotBlank(channel.getTopPostId())                         //该频道有置顶视频
                    && pageRequest.getPage() == 0) {                                          //第一页

                return getChannelPostsWithTopPost(query, channel, pageRequest.getPageSize());
            }
        } else {
            throw new IllegalArgumentException("paginationParam和pageRequest不能都为null");
        }

        //e.g. : find using query: { "$and" : [ { "searchTerms" : { "$all" : [ "#小编制作#"]}} , { "originPostId" :  null } , { "_id" : { "$ne" : { "$oid" : "5285b9c01a884fbe3e165681"}}}] , "_id" : { "$lt" : { "$oid" : "5285b9c01a884fbe3e165681"}} , "acl" :  null }
        return mongoTemplate.find(query, Post.class);
    }

    /**
     * @return queryCriteria, 最外层不可有"_id"key，否则会和com.lebo.service.AbstractMongoService#paginationById冲突
     */
    private Criteria parseChannelFollowAndTrack(String name, Channel channel) {
        //-- 解析follow和track begin --//
        String follow = null;
        String track;
        //未配置的频道，返回含有hashtag的内容
        if (channel == null) {
            track = "#" + name + "#";
        }
        //配置过的频道
        else {
            follow = channel.getFollow();
            track = channel.getTrack();
        }

        List<Criteria> criteriaList = new ArrayList<Criteria>(5);

        //解析follow条件
        Criteria followCriteria = null;
        if (StringUtils.isNotBlank(follow)) {
            String[] userIds = follow.split(Constants.COMMA_SEPARATOR);
            //用户ID之间or关系
            followCriteria = new Criteria(Post.USER_ID_KEY).in(Arrays.asList(userIds));
        }

        //解析track条件
        Criteria trackCriteria = null;
        if (StringUtils.isNotBlank(track)) {
            String[] phrases = track.split(Constants.COMMA_SEPARATOR);
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
                trackCriteria = orOperator(criterias);
            } else if (criterias.size() == 1) {
                trackCriteria = criterias.get(0);
            }
        }

        //follow + track --> 查询条件，or关系
        if (followCriteria != null && trackCriteria != null) {
            criteriaList.add(orOperator(Arrays.asList(followCriteria, trackCriteria)));
        } else {
            if (followCriteria != null) {
                criteriaList.add(followCriteria);
            }
            if (trackCriteria != null) {
                criteriaList.add(trackCriteria);
            }
        }
        //-- 解析follow和track end --//

        //频道不含转发贴
        criteriaList.add(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null));

        //排除置顶视频
        if (channel != null && StringUtils.isNotBlank(channel.getTopPostId())) {
            criteriaList.add(new Criteria(Post.ID_KEY).ne(new ObjectId(channel.getTopPostId())));
        }

        //各条件间是and关系
        Criteria queryCriteria = null;
        if (criteriaList.size() > 1) {
            queryCriteria = andOperator(criteriaList);
        } else if (criteriaList.size() == 1) {
            queryCriteria = criteriaList.get(0);
        }

        Assert.notNull(queryCriteria);
        return queryCriteria;
    }

    private List<Post> getChannelPostsWithTopPost(Query query, Channel channel, int count) {
        Post post = getPost(channel.getTopPostId());
        if (post != null) {
            query.limit(count - 1); //少查一项，给置顶留位置
            List<Post> posts = mongoTemplate.find(query, Post.class);

            if (posts.size() < count) { //在顶部插入置顶视频
                posts.add(0, post);
            }

            return posts;
        }
        return mongoTemplate.find(query, Post.class);
    }

    public List<Post> findDigest(PaginationParam paginationParam) {

        Setting setting = settingService.getSetting();

        Query query = new Query();

        query.addCriteria(new Criteria(Post.USER_ID_KEY).is(setting.getDigestAccountId()));

        //id条件, 用于排除置顶帖子和分页
        Criteria idCriteria = new Criteria(Post.ID_KEY);
        boolean addIdCriteria = false;

        //排除置顶帖子
        List<ObjectId> topPostIdList = parseObjectIds(setting.getDigestTopPostId());
        if (topPostIdList.size() > 0) {
            idCriteria.nin(topPostIdList);
            addIdCriteria = true;
        }

        //分页 begin
        if (!paginationParam.getMaxId().equals(MongoConstant.MONGO_ID_MAX_VALUE)) {
            idCriteria.lt(new ObjectId(paginationParam.getMaxId()));
            addIdCriteria = true;
        }

        if (!paginationParam.getSinceId().equals(MongoConstant.MONGO_ID_MIN_VALUE)) {
            idCriteria.gt(new ObjectId(paginationParam.getSinceId()));
            addIdCriteria = true;
        }

        if (addIdCriteria) {
            query.addCriteria(idCriteria);
        }

        query.with(paginationParam.getSort()).limit(paginationParam.getCount());
        //分页 end

        addAclPublicCriteria(query);

        //-- 添加置顶视频 begin --//
        if (topPostIdList.size() > 0
                && paginationParam.getMaxId().equals(MongoConstant.MONGO_ID_MAX_VALUE)) { //第一页

            List<Post> topPostList = getPostsWithOrder(topPostIdList);

            if (topPostList.size() > 0) {

                int queryCount = Math.max(0, paginationParam.getCount() - topPostList.size());

                if (queryCount == 0) {
                    return topPostList.subList(0, paginationParam.getCount());
                }

                query.limit(queryCount);

                List<Post> posts = mongoTemplate.find(query, Post.class);

                posts.addAll(0, topPostList);

                return posts;
            }
        }
        //-- 添加置顶视频 end --//

        return mongoTemplate.find(query, Post.class);
    }

    public List<Post> findUserDigest(String userId, PaginationParam paginationParam) {
        Setting setting = settingService.getSetting();

        Query query = new Query();
        query.addCriteria(new Criteria(Post.USER_ID_KEY).is(setting.getDigestAccountId()));
        query.addCriteria(new Criteria(Post.ORIGIN_POST_USER_ID_KEY).is(userId));
        paginationById(query, paginationParam);
        addAclPublicCriteria(query);

        return mongoTemplate.find(query, Post.class);
    }

    public int countUserDigest(String userId) {
        Setting setting = settingService.getSetting();
        Query query = new Query();
        query.addCriteria(new Criteria(Post.USER_ID_KEY).is(setting.getDigestAccountId()));
        query.addCriteria(new Criteria(Post.ORIGIN_POST_USER_ID_KEY).is(userId));
        return ((Long) mongoTemplate.count(query, Post.class)).intValue();
    }

    /**
     * 作品榜
     */
    public List<Post> rankingPosts(int page, int size) {
        Query query = new Query();
        //排除转发贴，因为转发贴不计收藏数(喜欢数)
        query.addCriteria(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null));
        //时间范围
        Date daysAgo = DateUtils.addDays(dateProvider.getDate(), settingService.getSetting().getRankingPostsDays() * -1);
        query.addCriteria(new Criteria(Post.CREATED_AT_KEY).gt(daysAgo));
        //按收藏数(喜欢数)降序排序
        query.with(new PageRequest(page, size, new Sort(Sort.Direction.DESC, Post.FAVOURITES_COUNT_KEY)));

        addAclPublicCriteria(query);

        return mongoTemplate.find(query, Post.class);
    }

    public void addPopularity(String postId, int amount) {
        mongoTemplate.updateFirst(
                new Query(new Criteria(Post.ID_KEY).is(postId)),
                new Update()
                        .inc(Post.POPULARITY_KEY, amount)
                        .inc(Post.FAVORITES_COUNT_ADD_POPULARITY_KEY, amount),
                Post.class
        );
    }

    private List<ObjectId> parseObjectIds(String str) {

        if (StringUtils.isBlank(str)) {
            return Collections.EMPTY_LIST;
        }

        String[] ids = str.split(Constants.COMMA_SEPARATOR);

        List<ObjectId> objectIdList = new ArrayList<ObjectId>(ids.length);

        for (String id : ids) {
            objectIdList.add(new ObjectId(id));
        }

        return objectIdList;
    }

    //-- 帖子管理 --//

    /**
     * 查找指定用户的帖子(不包含转发), 如果userId为null，则忽略该条件
     */
    public Page<Post> findOriginPosts(String userId, String track, String postId, PageRequest pageRequest, Date startDate, Date endDate) {
        Query query = new Query();
        //原帖
        query.addCriteria(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null));

        //帖子ID
        if (StringUtils.isNotBlank(postId)) {
            query.addCriteria(new Criteria(Post.ID_KEY).is(postId));
        }

        //指定用户
        if (StringUtils.isNotBlank(userId)) {
            query.addCriteria(new Criteria(Post.USER_ID_KEY).is(userId));
        }

        //搜索文字内容
        if (StringUtils.isNotBlank(track)) {
            query.addCriteria(new Criteria(Post.SEARCH_TERMS_KEY).is(track));
        }

        //日期范围
        if (startDate != null || endDate != null) {
            Criteria dateCriteria = new Criteria(Post.CREATED_AT_KEY);
            if (startDate != null) {
                dateCriteria.gte(startDate);
            }
            if (endDate != null) {
                dateCriteria.lt(endDate);
            }
            query.addCriteria(dateCriteria);
        }

        //分页、排序
        query.with(pageRequest);

        Page<Post> page = new PageImpl<Post>(mongoTemplate.find(query, Post.class),
                pageRequest,
                mongoTemplate.count(query, Post.class));

        //查找
        return page;
    }

    public void updateRating(String postId, int rating) {
        mongoTemplate.updateFirst(new Query(new Criteria(Post.ID_KEY).is(postId)),
                new Update().set(Post.RATING_KEY, rating),
                Post.class);
    }

    //---- JMX ----//

    /**
     * 刷新热门帖子列表:最近2天的帖子按红心数降序排序
     * 为避免刷屏，每用户只可上榜2条
     */
    @ManagedOperation(description = "刷新热门帖子列表")
    public void refreshHotPosts() {

        Comparator hotPostComparator = new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {

                Integer v1 = post1.getFavoritesCount()
                        + (post1.getRating() == null ? 0 : post1.getRating())
                        + (post1.getPopularity() == null ? 0 : post1.getPopularity());

                Integer v2 = post2.getFavoritesCount()
                        + (post2.getRating() == null ? 0 : post2.getRating())
                        + (post2.getPopularity() == null ? 0 : post2.getPopularity());

                return v2.compareTo(v1);
            }
        };

        logger.debug("更新热门帖子 : 开始");

        Date daysAgo = DateUtils.addDays(dateProvider.getDate(), settingService.getSetting().getHotDays() * -1);

        //2天内
        Query query = new Query(new Criteria(Post.CREATED_AT_KEY).gt(daysAgo));
        //原贴
        query.addCriteria(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null));
        //完全公开
        query.addCriteria(new Criteria(Post.ACL_KEY).is(Post.ACL_PUBLIC));
        //按红心数降序排序，取出2000条
        query.with(new PageRequest(0, 2000, hotPostsSort));

        List<Post> posts = mongoTemplate.find(query, Post.class);
        logger.debug("更新热门帖子 : 正在处理 {} 个帖子", posts.size());

        Collections.sort(posts, hotPostComparator);

        //为避免刷屏，每用户只可上榜1条
        int max = settingService.getSetting().getMaxHotPostCountPerUser();
        Map<String, Integer> userId2count = new HashMap<String, Integer>(1000);

        List<HotPost> hotPosts = new ArrayList<HotPost>(1000);
        for (Post post : posts) {

            Integer count = userId2count.get(post.getUserId());

            if (count == null) {
                count = 0;
                userId2count.put(post.getUserId(), 0);
            }

            if (count < max) {
                hotPosts.add(new HotPost(hotPosts.size() + 1, post.getId()));
                userId2count.put(post.getUserId(), count + 1);
            }
        }

        logger.debug("更新热门帖子 : 处理后得到 {} 个帖子", hotPosts.size());

        //更新热门帖子
        String collectionName = mongoTemplate.getCollectionName(HotPost.class);
        DBCollection tmpCollection = mongoTemplate.createCollection(String.format("%s.%s", collectionName, new ObjectId().toString()));

        mongoTemplate.insert(hotPosts, tmpCollection.getName());
        //重命名集合，原子性操作
        logger.debug("更新热门帖子 : 正在重命名集合 {} -> {}", tmpCollection.getName(), collectionName);
        tmpCollection.rename(collectionName, true);

        logger.debug("更新热门帖子 : 完成");
    }

    @ManagedOperation(description = "重新计算所有用户总帖子数、原始帖子数、转发帖子数")
    public void updateAllUserPostsCount() {
        String logPrefix = "更新所有用户总帖子数、原始帖子数、转发帖子数";

        logger.debug("{} : 开始", logPrefix);
        Query query = new Query();
        query.fields().include(User.ID_KEY);

        logger.debug("{} : 正在获取所有用户ID", logPrefix);
        List<User> users = mongoTemplate.find(query, User.class);
        logger.debug("{} : 共 {} 用户", logPrefix, users.size());

        for (int i = 0; i < users.size(); i++) {
            String userId = users.get(i).getId();

            if (i % 1000 == 0) {
                logger.debug("{} : 已完成 {}/{}", logPrefix, i, users.size());
            }

            mongoTemplate.updateFirst(
                    new Query(new Criteria(User.ID_KEY).is(userId)),
                    new Update()
                            .set(User.STATUSES_COUNT_KEY, countPost(userId, true, null))
                            .set(User.ORIGIN_POSTS_COUNT_KEY, countPost(userId, true, true))
                            .set(User.REPOSTS_COUNT_KEY, countPost(userId, null, false)),
                    User.class);
        }
        logger.debug("{} : 完成", logPrefix);
    }

    @ManagedOperation(description = "重新计算所有帖子的转发数、评论数")
    public void updateAllPostRepostsCountAndCommentsCount() {
        long beginTime = System.currentTimeMillis();
        String logPrefix = "更新所有帖子的转发数、评论数";

        logger.debug("{} : 开始", logPrefix);

        //查询
        Query query = new Query(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null))
                .addCriteria(new Criteria(Post.COMMENTS_COUNT_KEY).is(null))
                .with(new Sort(Sort.Direction.DESC, Post.ID_KEY))
                .limit(10000);
        query.fields().include(Post.ID_KEY);

        List<Post> posts = mongoTemplate.find(query, Post.class);

        int doneCount = 0;

        while (posts.size() > 0) {
            //更新
            for (int i = 0; i < posts.size(); i++) {
                String postId = posts.get(i).getId();

                mongoTemplate.updateFirst(
                        new Query(new Criteria(Post.ID_KEY).is(postId)),
                        new Update()
                                .set(Post.REPOSTS_COUNT_KEY, countReposts(postId))
                                .set(Post.COMMENTS_COUNT_KEY, commentService.countPostComments(postId)),
                        Post.class);

                doneCount++;
            }

            logger.debug("{} : {}", logPrefix, doneCount);

            //查询
            posts = mongoTemplate.find(query, Post.class);
        }

        logger.debug("{} : 完成，{} ms", logPrefix, System.currentTimeMillis() - beginTime);
    }

    @ManagedOperation(description = "计算用户原帖数、转贴数，仅处理未计算过的用户")
    public void updateAllUserOriginPostsCountAndRepostsCount() {

        long beginTime = System.currentTimeMillis();
        String prefix = "计算用户原帖数、转贴数: ";

        logger.debug("{}开始", prefix);

        Query query = new Query(new Criteria(User.ORIGIN_POSTS_COUNT_KEY).is(null));
        query.fields().include(User.ID_KEY);
        query.limit(10000);

        List<User> users = mongoTemplate.find(query, User.class);
        int totalCount = 0;

        while (users.size() > 0) {

            for (User user : users) {
                String userId = user.getId();
                mongoTemplate.updateFirst(
                        new Query(new Criteria(User.ID_KEY).is(userId)),
                        new Update()
                                .set(User.REPOSTS_COUNT_KEY, countPost(userId, null, false))
                                .set(User.ORIGIN_POSTS_COUNT_KEY, countPost(userId, true, true)),
                        User.class);
            }

            totalCount += users.size();
            logger.debug("{}{}", prefix, users.size());

            users = mongoTemplate.find(query, User.class);
        }

        logger.debug("{}完成, 共 {}, 用时 {} ms", prefix, totalCount, System.currentTimeMillis() - beginTime);
    }

    @ManagedOperation(description = "添加字段:Post.lastCommentCreatedAt")
    public void addPostFieldLastCommentCreatedAt() {

        class Q {
            List<Post> find(String maxId) {
                Query query = new Query(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null));
                query.addCriteria(new Criteria(Post.LAST_COMMENT_CREATED_AT_KEY).is(null));
                query.addCriteria(new Criteria(Post.ID_KEY).lt(new ObjectId(maxId)));
                query.with(new Sort(Sort.Direction.DESC, Post.ID_KEY));
                query.limit(2000);
                query.fields().include(Post.ID_KEY);
                return mongoTemplate.find(query, Post.class);
            }

            Date getLastCommentCreatedAt(Post post) {
                Query query = new Query(new Criteria(Comment.POST_ID_KEY).is(post.getId()));
                query.with(new Sort(Sort.Direction.DESC, Comment.ID_KEY));
                query.fields().include(Comment.CREATED_AT_KEY);

                Comment comment = mongoTemplate.findOne(query, Comment.class);

                return comment == null ?
                        null
                        :
                        comment.getCreatedAt();
            }
        }

        long beginTime = System.currentTimeMillis();
        String prefix = "添加字段:Post.lastCommentCreatedAt : ";
        int count = 0;

        Q q = new Q();
        List<Post> posts;
        String maxId = MongoConstant.MONGO_ID_MAX_VALUE;

        logger.debug("{}开始", prefix);

        while ((posts = q.find(maxId)).size() > 0) {
            for (Post post : posts) {

                Date commentCreatedAt = q.getLastCommentCreatedAt(post);

                if (commentCreatedAt != null) {

                    mongoTemplate.updateFirst(
                            new Query(new Criteria(Post.ID_KEY).is(post.getId())),
                            new Update().set(Post.LAST_COMMENT_CREATED_AT_KEY, commentCreatedAt),
                            Post.class);
                }
            }

            maxId = posts.get(posts.size() - 1).getId();

            count += posts.size();

            logger.debug("{}完成 {}", prefix, count);
        }

        logger.debug("{}完成 {} ms", prefix, System.currentTimeMillis() - beginTime);
    }

    @ManagedOperation(description = "添加字段:Post.favoritesCountAddPopularity")
    public void addPostFieldFavoritesCountAddPopularity() {

        class Q {

            String field = Post.FAVORITES_COUNT_ADD_POPULARITY_KEY;

            List<Post> find(String maxId) {
                Query query = new Query(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null));
                query.addCriteria(new Criteria(field).is(null));
                if (maxId != null) {
                    query.addCriteria(new Criteria(Post.ID_KEY).lt(new ObjectId(maxId)));
                }
                query.with(new Sort(Sort.Direction.DESC, Post.ID_KEY));
                query.limit(2000);
                query.fields().include(Post.FAVOURITES_COUNT_KEY).include(Post.POPULARITY_KEY);
                return mongoTemplate.find(query, Post.class);
            }
        }

        long beginTime = System.currentTimeMillis();
        String prefix = "添加字段 Post." + new Q().field + " : ";
        int count = 0;

        Q q = new Q();
        List<Post> posts;
        String maxId = null;

        logger.debug("{}开始", prefix);

        while ((posts = q.find(maxId)).size() > 0) {
            for (Post post : posts) {

                Integer popularity = post.getPopularity();

                Update update = new Update();

                if (popularity == null) {
                    popularity = 0;
                    update.set(Post.POPULARITY_KEY, 0);
                }

                update.set(Post.FAVORITES_COUNT_ADD_POPULARITY_KEY, post.getFavoritesCount() + popularity);

                mongoTemplate.updateFirst(
                        new Query(new Criteria(Post.ID_KEY).is(post.getId())),
                        update,
                        Post.class);
            }

            maxId = posts.get(posts.size() - 1).getId();

            count += posts.size();

            logger.debug("{}完成 {}", prefix, count);
        }

        logger.debug("{}完成 {} ms", prefix, System.currentTimeMillis() - beginTime);
    }
}
