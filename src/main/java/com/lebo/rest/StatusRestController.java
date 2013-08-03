package com.lebo.rest;

import com.lebo.entity.Post;
import com.lebo.entity.Setting;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.StatusDto;
import com.lebo.service.DuplicateException;
import com.lebo.service.SettingService;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-3
 * Time: PM8:35
 */
@Controller
@RequestMapping(value = "/api/1/statuses")
public class StatusRestController {
    public static final int ONE_M_BYTE = 1024 * 1024;
    @Autowired
    private StatusService statusService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private SettingService settingService;

    private Logger logger = LoggerFactory.getLogger(StatusRestController.class);

    //TODO 检查Post.text长度
    //TODO 保存source
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@RequestParam(value = "video") MultipartFile video,
                         @RequestParam(value = "image") MultipartFile image,
                         @RequestParam(value = "text") String text,
                         @RequestParam(value = "source", required = false) String source) {
        try {

            if (video.getSize() > ONE_M_BYTE || image.getSize() > ONE_M_BYTE) {
                return ErrorDto.badRequest(" Upload Single file size cannot be greater than 1 M byte.");
            }

            List<FileInfo> fileInfos = Arrays.asList(
                    new FileInfo(video.getInputStream(), video.getOriginalFilename(), video.getContentType()),
                    new FileInfo(image.getInputStream(), image.getOriginalFilename(), image.getContentType()));

            Post post = statusService.createPost(accountService.getCurrentUserId(), text, fileInfos, null, source);
            return statusService.toStatusDto(post);

        } catch (DuplicateException e) {
            return ErrorDto.duplicate();
        } catch (Exception e) {
            logger.info("发布Post失败", e);
            return ErrorDto.badRequest(e.getMessage());
        }
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
    @RequestMapping(value = "homeTimeline", method = RequestMethod.GET)
    @ResponseBody
    public Object homeTimeline(@Valid TimelineParam param) {
        param.setUserId(accountService.getCurrentUserId());
        List<Post> postList = statusService.homeTimeline(param);

        return statusService.toStatusDtos(postList);
    }

    /**
     * 获取某个用户最新视频列表
     */
    @RequestMapping(value = "userTimeline", method = RequestMethod.GET)
    @ResponseBody
    public Object userTimeline(@Valid TimelineParam param) {
        param.setUserId(accountService.getUserId(param.getUserId(), param.getScreenName()));
        List<Post> postList = statusService.userTimeline(param);

        return statusService.toStatusDtos(postList);
    }

    @RequestMapping(value = "mentionsTimeline", method = RequestMethod.GET)
    @ResponseBody
    public Object mentionsTimeline(@Valid TimelineParam param) {
        param.setUserId(accountService.getCurrentUserId());
        List<Post> postList = statusService.mentionsTimeline(param);

        return statusService.toStatusDtos(postList);
    }


    /**
     * 转发Post
     *
     * @param id   要转发的微博ID。
     * @param text 添加的转发文本，必须做URLencode，内容不超过140个汉字
     */
    @RequestMapping(value = "repost", method = RequestMethod.POST)
    @ResponseBody
    public Object repost(@RequestParam("id") String id,
                         @RequestParam(value = "text", required = false) String text,
                         @RequestParam(value = "source", required = false) String source) {
        try {
            Post post = statusService.findPost(id);
            if (post == null) {
                return ErrorDto.badRequest("The parameter id [" + id + "] is invalid.");
            }
            String originId = statusService.getOriginPostId(post);

            //转发
            if (!statusService.isReposted(accountService.getCurrentUserId(), originId)) {
                post = statusService.createPost(accountService.getCurrentUserId(), text, Collections.EMPTY_LIST, originId, source);
            }

            return statusService.toStatusDto(post);
        } catch (Exception e) {
            logger.info("转发Post失败", e);
            return ErrorDto.badRequest(e.getMessage());
        }
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
    @RequestMapping(value = "filter", method = RequestMethod.GET)
    @ResponseBody
    public Object filter(@Valid StatusFilterParam param) {
        return statusService.toStatusDtos(statusService.filterPosts(param));
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
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
            //搜索
            SearchParam param = new SearchParam(q, pageNo, size, direction, orderBy);
            param.setAfter(after);
            List<Post> posts = statusService.searchPosts(param);
            return statusService.toStatusDtos(posts);

        } else {
            return ErrorDto.badRequest(String.format("参数orderBy值[%s]无效", orderBy));
        }
    }

    @RequestMapping(value = "hot", method = RequestMethod.GET)
    @ResponseBody
    public Object hot(@RequestParam(value = "page", defaultValue = "0") int pageNo,
                      @RequestParam(value = "size", defaultValue = PaginationParam.DEFAULT_COUNT + "") int size) {
        Setting setting = settingService.getSetting();
        Date date = DateUtils.addDays(new Date(), setting.getHotDays() * -1);
        SearchParam param = new SearchParam(null, pageNo, size, Sort.Direction.DESC, Post.FAVOURITES_COUNT_KEY);
        param.setAfter(date);

        List<Post> posts = statusService.searchPosts(param);
        return statusService.toStatusDtos(posts);
    }

    @RequestMapping(value = "digest", method = RequestMethod.GET)
    @ResponseBody
    public Object digest(@Valid StatusFilterParam param) {
        Setting setting = settingService.getSetting();
        Date date = DateUtils.addDays(new Date(), setting.getHotDays() * -1);

        param.setFollow(setting.getOfficialAccountId());
        param.setAfter(date);

        List<Post> posts = statusService.filterPosts(param);
        return statusService.toStatusDtos(posts);
    }

    /**
     * 获取用户精品视频列表
     */
    @RequestMapping(value = "userDigest", method = RequestMethod.GET)
    @ResponseBody
    public Object userDigest(@Valid TimelineParam param) {
        param.setUserId(accountService.getUserId(param.getUserId(), param.getScreenName()));
        List<Post> posts = statusService.usreDigestline(param);
        return statusService.toStatusDtos(posts);
    }

    /**
     * 返回指定频道的post
     */
    @RequestMapping(value = "channelTimeline", method = RequestMethod.GET)
    @ResponseBody
    public Object channel(@RequestParam(value = "id") String id,
                          PaginationParam paginationParam) {
        if (StringUtils.isBlank(id)) {
            return ErrorDto.badRequest("id参数不能为空");
        }

        List<Post> posts = statusService.getChannelPosts(id, paginationParam);
        return statusService.toStatusDtos(posts);
    }


    @RequestMapping(value = "destroy", method = RequestMethod.POST)
    @ResponseBody
    public Object destroy(@RequestParam(value = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ErrorDto.badRequest("id参数不能为空");
        }

        Post post = statusService.findPost(id);

        if (!post.getUserId().equals(accountService.getCurrentUserId())) {
            return ErrorDto.unauthorized("你不是作者，没有权限删除");
        }

        StatusDto dto = statusService.toStatusDto(post);

        statusService.destroyPost(id);
        return dto;
    }

    @RequestMapping(value = "show", method = RequestMethod.GET)
    @ResponseBody
    public Object show(@RequestParam(value = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ErrorDto.badRequest("id参数不能为空");
        }

        Post post = statusService.findPost(id);

        if (post == null) {
            return ErrorDto.notFound("该视频不存在");
        }

        return statusService.toStatusDto(post);
    }

}
