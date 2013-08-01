package com.lebo.service;

import com.google.common.collect.Lists;
import com.lebo.entity.*;
import com.lebo.event.AfterCreatePostEvent;
import com.lebo.event.AfterDestroyPostEvent;
import com.lebo.event.ApplicationEventBus;
import com.lebo.repository.FollowingDao;
import com.lebo.repository.MongoConstant;
import com.lebo.repository.PostDao;
import com.lebo.repository.UserDao;
import com.lebo.rest.dto.StatusDto;
import com.lebo.rest.dto.UserDto;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.*;
import com.lebo.web.FileServlet;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
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
    @Autowired
    private SettingService settingService;
    @Autowired
    private ApplicationEventBus eventBus;


    /**
     * @param userId
     * @param text
     * @param fileInfos
     * @param originPostId 若是转发，为原Post ID，否则为null
     * @return
     * @throws IOException
     */
    public Post createPost(String userId, String text, List<FileInfo> fileInfos, String originPostId, String source) throws Exception {
        List<String> fileIds = gridFsService.saveFilesSafely(fileInfos);

        Post post = new Post().initial();
        post.setUserId(userId);
        post.setCreatedAt(new Date());
        post.setSource(source);
        post.setText(text);
        post.setUserMentions(mentionUserIds(text));
        post.setHashtags(findHashtags(text, true));
        post.setFiles(fileIds);
        post.setOriginPostId(originPostId);
        post.setSearchTerms(buildSearchTerms(post));

        post = postDao.save(post);
        throwOnMongoError();
        eventBus.post(new AfterCreatePostEvent(post));

        return post;
    }

    public Post destroyPost(String id) {
        Post post = postDao.findOne(id);
        if (post != null) {
            commentService.deleteByPostId(id);
            favoriteService.deleteByPostId(id);

            for(String fileId : post.getFiles()){
                gridFsService.delete(fileId);
            }

            postDao.delete(post);
            eventBus.post(new AfterDestroyPostEvent(post));
        }
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
     * 获取精品视频翻页
     *
     * @param param
     * @return
     */
    public List<Post> usreDigestline(TimelineParam param) {
        Assert.hasText(param.getUserId());
        return postDao.usreDigestline(param.getUserId(), param.getMaxId(), param.getSinceId(), param).getContent();
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

            //文件
            List<StatusDto.FileInfoDto> fileInfoDtos = new ArrayList<StatusDto.FileInfoDto>(2);
            for (String fileId : post.getFiles()) {
                fileInfoDtos.add(gridFsService.getFileInfoDto(fileId, "?" + FileServlet.POST_ID_KEY + "=" + post.getId()));
            }
            dto.setFiles(fileInfoDtos);

            //前3条评论
            CommentListParam commentListParam = new CommentListParam();
            commentListParam.setCount(3);
            commentListParam.setPostId(post.getId());
            List<Comment> comments = commentService.list(commentListParam);
            dto.setComments(commentService.toCommentDtos(comments));

            //提到的用户
            if (!CollectionUtils.isEmpty(post.getUserMentions())) {
                List<UserDto> userMetions = new ArrayList<UserDto>(post.getUserMentions().size());
                for (String userId : post.getUserMentions()) {
                    User user = accountService.getUser(userId);
                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setScreenName(user.getScreenName());
                    userMetions.add(userDto);
                }
                dto.setUserMentions(userMetions);
            }

            //是否被当前登录用户转发
            dto.setReposted(isReposted(accountService.getCurrentUserId(), post.getId()));

            /* 已经添加精华字段
            //是否是加精的
            Setting setting = settingService.getSetting();
            dto.setDigested(setting.getDigestFollow().contains(post.getUserId()));
            */
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

    private Pattern mentionPattern = Pattern.compile("@([^@#\\s]+)");

    public LinkedHashSet<String> mentionScreenNames(String text, boolean trimAt) {
        if (StringUtils.isBlank(text)) {
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
        words.addAll(mentionScreenNames(post.getText(), false));
        words.addAll(segmentation.findWords(post.getText()));
        return words;
    }

    public boolean isReposted(String userId, String postId) {
        Query query = new Query();
        query.addCriteria(new Criteria(Post.USER_ID_KEY).is(userId));
        query.addCriteria(new Criteria(Post.ORIGIN_POST_ID_KEY).is(postId));
        return mongoTemplate.count(query, Post.class) > 0;
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

    /**
     * 查找某频道的Posts，按ID降序排序，按ID分页。
     */
    //TODO 精简代码
    //(follow || track) && page
    public List<Post> getChannelPosts(String id, PaginationParam paginationParam) {
        //查找配置中的channel
        Setting.Channel channel = null;
        List<Setting.Channel> channels = settingService.getSetting().getChannels();
        for (Setting.Channel c : channels) {
            if (id.equals(c.getId())) {
                channel = c;
                break;
            }
        }

        String follow = null;
        String track = null;
        //未配置的频道，返回含有hashtag的内容
        if (channel == null) {
            track = "#" + id + "#";
            //配置过的频道
        } else {
            follow = channel.getFollow();
            track = channel.getTrack();
        }

        List<Criteria> criteriaList = new ArrayList<Criteria>(5);
        Criteria followCriteria = null;
        if (StringUtils.isNotBlank(follow)) {
            String[] userIds = follow.split("\\s*,\\s*");
            //用户ID之间or关系
            followCriteria = new Criteria(Post.USER_ID_KEY).in(Arrays.asList(userIds));
        }

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

        //分页
        if (!paginationParam.getMaxId().equals(MongoConstant.MONGO_ID_MAX_VALUE)) {
            criteriaList.add(new Criteria("_id").lt(new ObjectId(paginationParam.getMaxId())));
        }
        if (!paginationParam.getSinceId().equals(MongoConstant.MONGO_ID_MIN_VALUE)) {
            criteriaList.add(new Criteria("_id").gt(new ObjectId(paginationParam.getSinceId())));
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
        query.with(PaginationParam.DEFAULT_SORT).limit(paginationParam.getCount());

        return mongoTemplate.find(query, Post.class);
    }
}
