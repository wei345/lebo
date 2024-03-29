package com.lebo.rest;

import com.lebo.Constants;
import com.lebo.entity.*;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.HotDto;
import com.lebo.rest.dto.StatusDto;
import com.lebo.service.*;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.*;
import com.lebo.web.ControllerUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-3
 * Time: PM8:35
 */
@Controller
public class StatusRestController {

    private static final String API_1_PREFIX = "/api/1/statuses/";
    private static final String API_1_1_PREFIX = "/api/1.1/statuses/";

    @Autowired
    private StatusService statusService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ALiYunStorageService aLiYunStorageService;
    @Autowired
    private AdService adService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private UploadService uploadService;

    private Logger logger = LoggerFactory.getLogger(StatusRestController.class);

    //TODO 检查Post.text长度
    @RequestMapping(value = API_1_PREFIX + "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@RequestParam(value = "video") MultipartFile video,
                         @RequestParam(value = "image") MultipartFile image,
                         @RequestParam(value = "text") String text,
                         @RequestParam(value = "source", required = false) String source) throws IOException {
        logger.debug("正在发布新视频:");
        logger.debug("      video : {}", FileUtils.byteCountToDisplaySize(video.getSize()));
        logger.debug("      image : {}", FileUtils.byteCountToDisplaySize(image.getSize()));
        logger.debug("       text : {}", text);
        logger.debug("     source : {}", source == null ? "无" : source);

        if (video.getSize() > Setting.MAX_VIDEO_LENGTH_BYTES || image.getSize() > Setting.MAX_IMAGE_LENGTH_BYTES) {
            return ErrorDto.badRequest("上传的视频("
                    + FileUtils.byteCountToDisplaySize(video.getSize())
                    + ")或图片("
                    + FileUtils.byteCountToDisplaySize(image.getSize())
                    + ")太大");
        }

        return createPost(text, ControllerUtils.getFileInfo(video), ControllerUtils.getFileInfo(image), source, Post.ACL_PUBLIC);
    }

    /**
     * <h2>Twitter</h2>
     * <p>
     * Returns a collection of the most recent Tweets and retweets posted by
     * the authenticating user and the users they follow. The home timeline
     * is central to how most users interact with the Twitter service.
     * </p>
     * <p>
     * Up to 800 Tweets are obtainable on the home timeline. It is more
     * volatile for users that follow many users or follow users who tweet
     * frequently.
     * </p>
     * <p>
     * https://dev.twitter.com/docs/api/1.1/get/statuses/home_timeline
     * </p>
     */
    @RequestMapping(value = API_1_PREFIX + "homeTimeline", method = RequestMethod.GET)
    @ResponseBody
    public Object homeTimeline(@Valid TimelineParam param) {
        param.setUserId(accountService.getCurrentUserId());
        List<Post> postList = statusService.homeTimeline(param);

        return statusService.toStatusDtos(postList);
    }

    /**
     * 获取某个用户最新视频列表
     */
    @RequestMapping(value = API_1_PREFIX + "userTimeline", method = RequestMethod.GET)
    @ResponseBody
    public Object userTimeline(@Valid TimelineParam param) {
        param.setUserId(accountService.getUserId(param.getUserId(), param.getScreenName()));
        List<Post> postList = statusService.userTimeline(param);

        return statusService.toStatusDtos(postList);
    }

    @RequestMapping(value = API_1_PREFIX + "mentionsTimeline", method = RequestMethod.GET)
    @ResponseBody
    public Object mentionsTimeline(@Valid TimelineParam param) {
        param.setUserId(accountService.getCurrentUserId());
        List<Post> postList = statusService.mentionsTimeline(param);

        return statusService.toStatusDtos(postList);
    }


    /**
     * 转发Post
     *
     * @param id   原始post id或转发post id
     * @param text 添加的转发文本，必须做URLencode，内容不超过140个汉字
     */
    @RequestMapping(value = API_1_PREFIX + "repost", method = RequestMethod.POST)
    @ResponseBody
    public Object repost(@RequestParam("id") String id,
                         @RequestParam(value = "text", required = false) String text,
                         @RequestParam(value = "source", required = false) String source) {
        try {
            Post originPost = statusService.getPost(id);
            if (originPost == null) {
                return ErrorDto.badRequest("原始视频[" + id + "]不存在.");
            }

            //确保转发原帖
            if (originPost.getOriginPostId() != null) {
                originPost = statusService.getPost(originPost.getOriginPostId());
                if (originPost == null) {
                    return ErrorDto.badRequest("原始视频[" + originPost.getOriginPostId() + "]不存在.");
                }
            }

            if (!originPost.isPublic()) {
                return ErrorDto.badRequest("不能转发非所有人可见的视频");
            }

            //转发
            Post post = statusService.getRepost(accountService.getCurrentUserId(), originPost);
            if (post == null) {
                post = statusService.createPost(accountService.getCurrentUserId(), text, null, null, originPost, source, Post.ACL_PUBLIC);
            }
            return statusService.toStatusDto(post);

        } catch (Exception e) {
            logger.info("转发Post失败", e);
            return ErrorDto.badRequest(e.getMessage());
        }
    }

    /**
     * 取消转发Post
     *
     * @param id 原始帖ID
     */
    @RequestMapping(value = API_1_PREFIX + "unrepost", method = RequestMethod.POST)
    @ResponseBody
    public Object unrepost(@RequestParam("id") String id) {

        String currentUserId = accountService.getCurrentUserId();

        Post post = statusService.getRepost(currentUserId, id);

        if (post != null) {
            statusService.destroyPost(post.getId());

            StatusDto dto = statusService.toBasicStatusDto(post);

            dto.getOriginStatus().setReposted(
                    statusService.isReposted(currentUserId, post));

            dto.getOriginStatus().setRepostsCount(
                    statusService.countReposts(post.getOriginPostId()));

            return dto;
        }

        return null;
    }

    /**
     * <ul><li>以逗号分隔多个短语</li>
     * <li>一个短语包含一个或多个词，以空格分隔</li>
     * <li>如果Post.terms出现短语中的每个词，则认为匹配该短语，词顺序无关，忽略大小写</li>
     * <li>也就是空格相当于AND，逗号相当于OR</li>
     * <li>查找范围包括：text、expanded_url、display_url、hashtags、screen_name</li>
     * <li>每个短语1-60字节</li>
     * <li>不支持短语精确匹配</li>
     * <li>#hashtag或@mention不会匹配#hashtags或@mentions。</li>
     * </ul>
     */
    @RequestMapping(value = API_1_PREFIX + "filter", method = RequestMethod.GET)
    @ResponseBody
    public Object filter(@Valid StatusFilterParam param) {
        return statusService.toStatusDtos(statusService.filterPosts(param));
    }

    @RequestMapping(value = API_1_PREFIX + "search", method = RequestMethod.GET)
    @ResponseBody
    public Object search(@RequestParam(value = "q", required = false) String q,
                         @RequestParam(value = "page", defaultValue = "0") int pageNo,
                         @RequestParam(value = "size", defaultValue = PaginationParam.DEFAULT_COUNT + "") int size,
                         @RequestParam(value = "orderBy", defaultValue = "_id") String orderBy,
                         @RequestParam(value = "order", defaultValue = "desc") String order,
                         @RequestParam(value = "after", required = false) Date after) {
        if (orderBy.equals("id")) {
            orderBy = "_id";
        }

        Sort.Direction direction;
        if (order.equals("desc")) {
            direction = Sort.Direction.DESC;
        } else if (order.equals("asc")) {
            direction = Sort.Direction.ASC;
        } else {
            return ErrorDto.badRequest(String.format("参数order值[%s]无效", order));
        }

        if (Post.FAVOURITES_COUNT_KEY.equals(orderBy) ||
                Post.VIEW_COUNT_KEY.equals(orderBy) ||
                "_id".equals(orderBy)) {
            //TODO 排序有问题，转发贴没有计数
            //搜索
            SearchParam param = new SearchParam(q, pageNo, size, direction, orderBy);
            param.setAfter(after);
            List<Post> posts = statusService.searchPosts(param);
            return statusService.toStatusDtos(posts);

        } else {
            return ErrorDto.badRequest(String.format("参数orderBy值[%s]无效", orderBy));
        }
    }

    @RequestMapping(value = API_1_PREFIX + "hot", method = RequestMethod.GET)
    @ResponseBody
    public Object hot(@RequestParam(value = "page", defaultValue = "0") int page,
                      @RequestParam(value = "size", defaultValue = PaginationParam.DEFAULT_COUNT + "") int size) {
        return statusService.toStatusDtos(statusService.hotPosts(page, size));
    }

    @RequestMapping(value = API_1_PREFIX + "digest", method = RequestMethod.GET)
    @ResponseBody
    public Object digest(@Valid PaginationParam param) {
        List<Post> posts = statusService.findDigest(param);
        return statusService.toStatusDtos(posts);
    }

    /**
     * 返回用户精品视频列表
     */
    @RequestMapping(value = API_1_PREFIX + "userDigest", method = RequestMethod.GET)
    @ResponseBody
    public Object userDigest(@Valid PaginationParam param,
                             @RequestParam(value = "userId", required = false) String userId,
                             @RequestParam(value = "screenName", required = false) String screenName) {
        userId = accountService.getUserId(userId, screenName);
        List<Post> posts = statusService.findUserDigest(userId, param);
        return statusService.toStatusDtos(posts);
    }

    /**
     * 返回指定频道的post
     */
    @RequestMapping(value = API_1_PREFIX + "channelTimeline", method = RequestMethod.GET)
    @ResponseBody
    public Object channelTimeline(@RequestParam(value = "name") String name,
                                  PaginationParam paginationParam) {
        if (StringUtils.isBlank(name)) {
            return ErrorDto.badRequest("name参数不能为空");
        }

        List<Post> posts = statusService.getChannelPosts(name, paginationParam);
        return statusService.toStatusDtos(posts);
    }


    @RequestMapping(value = API_1_PREFIX + "destroy", method = RequestMethod.POST)
    @ResponseBody
    public Object destroy(@RequestParam(value = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ErrorDto.badRequest("id参数不能为空");
        }

        Post post = statusService.getPost(id);

        if (!post.getUserId().equals(accountService.getCurrentUserId())) {
            return ErrorDto.forbidden("你不是作者，没有权限删除");
        }

        StatusDto dto = statusService.toStatusDto(post);

        statusService.destroyPost(id);
        return dto;
    }

    @RequestMapping(value = API_1_PREFIX + "show", method = RequestMethod.GET)
    @ResponseBody
    public Object show(@RequestParam(value = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ErrorDto.badRequest("id参数不能为空");
        }

        Post post = statusService.getPost(id);

        if (post == null) {
            return ErrorDto.notFound("该视频不存在");
        }

        return statusService.toStatusDto(post);
    }

    /**
     * 作品榜：按收藏数(红心)降序排序
     */
    @RequestMapping(value = API_1_PREFIX + "ranking", method = RequestMethod.GET)
    @ResponseBody
    public Object ranking(@RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = PaginationParam.DEFAULT_COUNT + "") int size) {
        return statusService.toStatusDtos(statusService.rankingPosts(page, size));
    }

    /**
     * 视频播放次数+1
     */
    @RequestMapping(value = API_1_PREFIX + "increaseViewCountBatch", method = RequestMethod.POST)
    @ResponseBody
    public Object increaseViewCount(@RequestParam(value = "postIds") String postIds,
                                    @RequestParam(value = "userIds") String userIds) {
        statusService.increaseViewCount(Arrays.asList(postIds.split(Constants.COMMA_SEPARATOR)));
        accountService.increaseViewCount(Arrays.asList(userIds.split(Constants.COMMA_SEPARATOR)));
        return ErrorDto.OK;
    }

    //-- v1.1 --//
    @RequestMapping(value = API_1_1_PREFIX + "update.json", method = RequestMethod.POST)
    @ResponseBody
    public Object update_v1_1(@RequestParam("videoUrl") String videoUrl,
                              @RequestParam("imageUrl") String imageUrl,
                              @RequestParam("duration") Long duration,
                              @RequestParam(value = "text", defaultValue = "") String text,
                              @RequestParam(value = "source", required = false) String source,
                              @RequestParam(value = "pvt", defaultValue = "false") boolean pvt) throws IOException {

        FileInfo videoFileInfo = aLiYunStorageService.getTmpFileInfoFromUrl(videoUrl);
        videoFileInfo.setDuration(duration);
        FileInfo imageFileInfo = aLiYunStorageService.getTmpFileInfoFromUrl(imageUrl);

        try {
            uploadService.checkVideo(videoFileInfo);
            uploadService.checkImage(imageFileInfo);

        } catch (ServiceException e) {

            aLiYunStorageService.delete(videoFileInfo.getTmpKey());
            aLiYunStorageService.delete(imageFileInfo.getTmpKey());

            throw e;
        }

        Integer acl = pvt ? Post.ACL_PRIVATE : Post.ACL_PUBLIC;

        return createPost(text, videoFileInfo, imageFileInfo, source, acl);
    }

    private Object createPost(String text, FileInfo video, FileInfo videoFirstFrame, String source, Integer acl) {
        //去掉 #最新视频#，永远不需要这个标签
        String newPostHashtag = "#最新视频#";
        if (text.contains(newPostHashtag)) {
            text = text.replace(newPostHashtag, "");
            logger.debug("自动去掉了{} : {}", newPostHashtag, text);
        }

        //当且仅当用户第一次发视频时，有"#新人报到#"
        String firstVideoHashtag = "#新人报到#";
        User user = accountService.getUser(accountService.getCurrentUserId());
        //用户第一次发视频
        if (acl == Post.ACL_PUBLIC
                && (user.getStatusesCount() == null || user.getStatusesCount() == 0)) {
            if (!text.contains(firstVideoHashtag)) {
                text += firstVideoHashtag;
                logger.debug("自动添加了{} : {}", firstVideoHashtag, text);
            }
        }
        //不是第一次
        else {
            if (text.contains(firstVideoHashtag)) {
                text = text.replace(firstVideoHashtag, "");
                logger.debug("不是该用户第一个视频，自动去掉了{} : {}", firstVideoHashtag, text);
            }
        }

        Post post = statusService.createPost(accountService.getCurrentUserId(), text, video,
                videoFirstFrame, null, source, acl);

        return statusService.toStatusDto(post);
    }

    /**
     * 热门页:用户列表，按红心数排序，有广告
     */
    @RequestMapping(value = API_1_1_PREFIX + "hot.json", method = RequestMethod.GET)
    @ResponseBody
    public Object hot_v1_1(@Valid PageRequest pageRequest,
                           @RequestParam(value = "ads", defaultValue = "true") boolean ads) {
        HotDto dto = new HotDto();

        if (ads) {
            dto.setAds(adService.toDtos(adService.findAd(Ad.GROUP_HOT, true)));
            dto.setAdsExpanded(settingService.getSetting().isAdsHotExpanded());
        }

        List<StatusDto> statuses = statusService.toStatusDtos(
                statusService.hotPosts(pageRequest.getPage(), pageRequest.getSize()));
        dto.setStatuses(statuses);

        return dto;
    }

    /**
     * 帖子分享次数+1
     */
    @RequestMapping(value = API_1_1_PREFIX + "increaseShareCount.json", method = RequestMethod.POST)
    @ResponseBody
    public Object increaseShareCount(@RequestParam(value = "postId") String postId) {
        statusService.increaseShareCount(postId);
        return ErrorDto.OK;
    }

    /**
     * 返回指定频道的post
     */
    @RequestMapping(value = API_1_1_PREFIX + "channel.json", method = RequestMethod.GET)
    @ResponseBody
    public Object channel_v1_1(@RequestParam(value = "name") String name,
                               @RequestParam(value = "orderBy") String orderBy,
                               PageRequest pageRequest) {

        if (StringUtils.isBlank(name)) {
            return ErrorDto.badRequest("name参数不能为空");
        }

        Sort sort;

        if (Post.CREATED_AT_KEY.equals(orderBy)) {

            sort = new Sort(Sort.Direction.DESC, orderBy);

        } else if (Post.LAST_COMMENT_CREATED_AT_KEY.equals(orderBy)
                || Post.FAVOURITES_COUNT_KEY.equals(orderBy)) {

            sort = new Sort(Sort.Direction.DESC, orderBy, Post.ID_KEY);

        } else if ("hot".equals(orderBy)) {

            sort = new Sort(Sort.Direction.DESC, Post.FAVORITES_COUNT_ADD_POPULARITY_KEY, Post.ID_KEY);

        } else {

            return ErrorDto.badRequest("不支持的orderBy [" + orderBy + "]");
        }

        pageRequest.setSort(sort);

        List<Post> posts = statusService.getChannelPosts(name, pageRequest);

        return statusService.toStatusDtos(posts);
    }

}
