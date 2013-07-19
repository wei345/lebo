package com.lebo.rest;

import com.lebo.entity.Post;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.DuplicateException;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.FileInfo;
import com.lebo.service.param.SearchParam;
import com.lebo.service.param.StatusFilterParam;
import com.lebo.service.param.TimelineParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
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
                return ErrorDto.newBadRequestError(" Upload Single file size cannot be greater than 1 M byte.");
            }

            List<FileInfo> fileInfos = Arrays.asList(
                    new FileInfo(video.getInputStream(), video.getOriginalFilename(), video.getContentType()),
                    new FileInfo(image.getInputStream(), image.getOriginalFilename(), image.getContentType()));

            return statusService.update(accountService.getCurrentUserId(), text, fileInfos, null, source);

        } catch (DuplicateException e) {
            return ErrorDto.DUPLICATE;
        } catch (Exception e) {
            logger.info("发布Post失败", e);
            return ErrorDto.newBadRequestError(e.getMessage());
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

        return statusService.toStatusDtoList(postList);
    }

    /**
     * <h2>Twitter</h2>
     * <p>
     * Returns a collection of the most recent Tweets posted by the user
     * indicated by the screen_name or user_id parameters.
     * </p>
     * <p>
     * User timelines belonging to protected users may only be requested
     * when the authenticated user either "owns" the timeline or is an
     * approved follower of the owner.
     * </p>
     * <p>
     * The timeline returned is the equivalent of the one seen when you
     * view a user's profile on twitter.com.
     * </p>
     * <p>
     * This method can only return up to 3,200 of a user's most recent
     * Tweets. Native retweets of other statuses by the user is included
     * in this total, regardless of whether include_rts is set to false
     * when requesting this resource.
     * </p>
     * <p>
     * https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline
     * </p>
     * <p/>
     * <h2>新浪</h2>
     * <p>
     * 获取某个用户最新发表的微博列表
     * </p>
     * <p>
     * http://open.weibo.com/wiki/2/statuses/user_timeline
     * </p>
     */
    @RequestMapping(value = "userTimeline", method = RequestMethod.GET)
    @ResponseBody
    public Object userTimeline(@Valid TimelineParam param) {
        param.setUserId(accountService.getCurrentUserId());
        List<Post> postList = statusService.userTimeline(param);

        return statusService.toStatusDtoList(postList);
    }

    @RequestMapping(value = "mentionsTimeline", method = RequestMethod.GET)
    @ResponseBody
    public Object mentionsTimeline(@Valid TimelineParam param) {
        param.setUserId(accountService.getCurrentUserId());
        List<Post> postList = statusService.mentionsTimeline(param);

        return statusService.toStatusDtoList(postList);
    }

    /**
     * 转发Post
     *
     * @param id   要转发的微博ID。
     * @param text 添加的转发文本，必须做URLencode，内容不超过140个汉字
     */
    @RequestMapping(value = "repost", method = RequestMethod.POST)
    @ResponseBody
    private Object repost(@RequestParam("id") String id,
                          @RequestParam(value = "text", required = false) String text,
                          @RequestParam(value = "source", required = false) String source) {
        try {
            Post post = statusService.findPost(id);
            if (post == null) {
                return ErrorDto.newBadRequestError("The parameter id [" + id + "] is invalid.");
            }
            String originId = statusService.getOriginPostId(post);

            //转发
            if(!statusService.isReposted(accountService.getCurrentUserId(), originId)){
                post = statusService.update(accountService.getCurrentUserId(), text, Collections.EMPTY_LIST, originId, source);
            }

            return statusService.toStatusDto(post);
        }  catch (Exception e) {
            logger.info("转发Post失败", e);
            return ErrorDto.newBadRequestError(e.getMessage());
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
        return statusService.toStatusDtoList(statusService.searchPosts(param));
    }
}
