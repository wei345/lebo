package com.lebo.service;

import com.google.common.collect.Lists;
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
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    private static final String FILE_COLLECTION_NAME = "post";

    /**
     * @param userId
     * @param text
     * @param video
     * @param videoFirstFrame 视频第一帧
     * @param originPost      若是转发，为原Post ID，否则为null
     * @return
     * @throws IOException
     */
    public Post createPost(String userId, String text, FileInfo video, FileInfo videoFirstFrame, Post originPost, String source) throws Exception {
        Post post = new Post().initial();

        post.setUserId(userId);
        post.setCreatedAt(new Date());
        post.setId(newMongoId(post.getCreatedAt()));
        post.setSource(source);
        post.setText(text);
        post.setUserMentions(findUserMentions(text));
        post.setMentionUserIds(mentionUserIds(post.getUserMentions()));
        post.setHashtags(findHashtags(text, true));
        post.setSearchTerms(buildSearchTerms(post));

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

            for (FileInfo fileInfo : post.getFiles()) {
                fileStorageService.delete(fileInfo.getKey());
            }

            postDao.delete(post);
            eventBus.post(new AfterPostDestroyEvent(post));
        }
        return post;
    }

    private void deleteReposts(String originPostId) {
        mongoTemplate.remove(new Query(new Criteria(Post.ORIGIN_POST_ID_KEY).is(originPostId)), Post.class);
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
        List<String> followingIdList = friendshipService.getAllFriends(param.getUserId());

        followingIdList.add(param.getUserId());

        return postDao.homeTimeline(followingIdList, param.getMaxId(), param.getSinceId(), param).getContent();
    }

    public List<Post> mentionsTimeline(TimelineParam param) {
        Assert.hasText(param.getUserId(), "The userId can not be null");
        return postDao.mentionsTimeline(param.getUserId(), param.getMaxId(), param.getSinceId(), param);
    }

    public Post getPost(String id) {
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

    public StatusDto toBasicStatusDto(Post post) {
        StatusDto dto = BeanMapper.map(post, StatusDto.class);
        dto.setUser(accountService.toBasicUserDto(accountService.getUser(post.getUserId())));

        //转发贴
        if (post.getOriginPostId() != null) {
            Post originPost = postDao.findOne(post.getOriginPostId());
            dto.setOriginStatus(toBasicStatusDto(originPost));
        }

        return dto;
    }

    public StatusDto toStatusDto(Post post) {
        StatusDto dto = BeanMapper.map(post, StatusDto.class);
        dto.setUser(accountService.toUserDto(accountService.getUser(post.getUserId())));

        //原帖
        if (post.getOriginPostId() == null) {
            dto.setRepostsCount(countReposts(post.getId()));
            dto.setReposted(isReposted(accountService.getCurrentUserId(), post));
            dto.setFavorited(favoriteService.isFavorited(accountService.getCurrentUserId(), post.getId()));
            dto.setCommentsCount(commentService.countPostComments(post.getId()));

            //前3条评论
            CommentListParam commentListParam = new CommentListParam();
            //TODO 评论数量可配置
            commentListParam.setCount(3);
            commentListParam.setPostId(post.getId());
            List<Comment> comments = commentService.list(commentListParam);
            dto.setComments(commentService.toCommentDtos(comments));

            //是否被当前登录用户转发
            dto.setReposted(isReposted(accountService.getCurrentUserId(), post));
        }
        //转发贴
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
            String[] userIds = param.getFollow().split("\\s*,\\s*");
            //用户ID之间or关系
            criteriaList.add(new Criteria(Post.USER_ID_KEY).in(Arrays.asList(userIds)));
        }

        //track
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
        query.with(PaginationParam.DEFAULT_SORT).limit(param.getCount());

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

    /*private static final Sort hotPostsSort = new Sort(Sort.Direction.DESC, HotPost.HOT_FAVOURITES_COUNT_KEY);
    /*
     * 按2天内收到的红心数(收藏数)排序
     */
    /*public List<StatusDto> hotPosts(Integer page, Integer size) {
        List<HotPost> hotPosts = mongoTemplate.find(new Query().with(new PageRequest(page, size, hotPostsSort)), HotPost.class);

        List<StatusDto> dtos = new ArrayList<StatusDto>(hotPosts.size());
        for (HotPost hotPost : hotPosts) {
            StatusDto dto = toStatusDto(getPost(hotPost.getId()));
            dto.setHotFavoritesCount(hotPost.getHotFavoritesCount());
            dtos.add(dto);
        }
        return dtos;
    }*/
    private static final Sort hotPostsSort = new Sort(Sort.Direction.DESC, Post.FAVOURITES_COUNT_KEY);

    /**
     * 热门:最近2天的帖子按红心数降序排序
     */
    public List<StatusDto> hotPosts(Integer page, Integer size) {
        Date daysAgo = DateUtils.addDays(dateProvider.getDate(), settingService.getSetting().getHotDays() * -1);
        Query query = new Query(new Criteria(Post.CREATED_AT_KEY).gt(daysAgo));
        query.addCriteria(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null));
        query.with(new PageRequest(page, size, hotPostsSort));
        List<Post> posts = mongoTemplate.find(query, Post.class);
        return toStatusDtos(posts);
    }

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

    private Pattern mentionPattern = Pattern.compile("@([^@#\\s]+)");

    public LinkedHashSet<String> mentionUserIds(List<Post.UserMention> userMentions) {
        LinkedHashSet<String> userIds = new LinkedHashSet<String>();
        for(Post.UserMention userMention : userMentions){
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
        for(Post.UserMention userMention : post.getUserMentions()){
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
    //TODO 精简代码
    //(follow || track) && page
    public List<Post> getChannelPosts(String name, PaginationParam paginationParam) {
        //-- 解析follow和track begin --//
        //查找配置中的channel
        Channel channel = null;
        List<Channel> channels = settingService.getSetting().getChannels();
        for (Channel c : channels) {
            if (name.equals(c.getTitle())) {
                channel = c;
                break;
            }
        }

        String follow = null;
        String track = null;
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
            String[] userIds = follow.split("\\s*,\\s*");
            //用户ID之间or关系
            followCriteria = new Criteria(Post.USER_ID_KEY).in(Arrays.asList(userIds));
        }

        //解析track条件
        Criteria trackCriteria = null;
        if (StringUtils.isNotBlank(track)) {
            String[] phrases = track.split("\\s*,\\s*");
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

        //分页
        if (!paginationParam.getMaxId().equals(MongoConstant.MONGO_ID_MAX_VALUE)) {
            criteriaList.add(new Criteria("_id").lt(new ObjectId(paginationParam.getMaxId())));
        }
        if (!paginationParam.getSinceId().equals(MongoConstant.MONGO_ID_MIN_VALUE)) {
            criteriaList.add(new Criteria("_id").gt(new ObjectId(paginationParam.getSinceId())));
        }

        //各条件间是and关系
        Criteria queryCriteria = null;
        if (criteriaList.size() > 1) {
            queryCriteria = andOperator(criteriaList);
        } else if (criteriaList.size() == 1) {
            queryCriteria = criteriaList.get(0);
        }

        //根据条件执行查询
        Query query = new Query();
        if (queryCriteria != null) {
            query.addCriteria(queryCriteria);
        }
        query.with(PaginationParam.DEFAULT_SORT).limit(paginationParam.getCount());

        return mongoTemplate.find(query, Post.class);
    }

    public List<Post> findDigest(PaginationParam paginationParam) {
        Setting setting = settingService.getSetting();
        Query query = new Query();
        query.addCriteria(new Criteria(Post.USER_ID_KEY).is(setting.getOfficialAccountId()));
        paginationById(query, paginationParam);

        return mongoTemplate.find(query, Post.class);
    }

    public List<Post> findUserDigest(String userId, PaginationParam paginationParam) {
        Setting setting = settingService.getSetting();
        Query query = new Query();
        query.addCriteria(new Criteria(Post.USER_ID_KEY).is(setting.getOfficialAccountId()));
        query.addCriteria(new Criteria(Post.ORIGIN_POST_USER_ID_KEY).is(userId));
        paginationById(query, paginationParam);

        return mongoTemplate.find(query, Post.class);
    }

    public int countUserDigest(String userId) {
        Setting setting = settingService.getSetting();
        Query query = new Query();
        query.addCriteria(new Criteria(Post.USER_ID_KEY).is(setting.getOfficialAccountId()));
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
        //按收藏数(喜欢数)降序排序
        query.with(new PageRequest(page, size, new Sort(Sort.Direction.DESC, Post.FAVOURITES_COUNT_KEY)));

        return mongoTemplate.find(query, Post.class);
    }
}
