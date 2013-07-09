package com.lebo.rest;

import com.google.common.collect.Lists;
import com.lebo.entity.Tweet;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.StatusDto;
import com.lebo.service.DuplicateException;
import com.lebo.service.StatusService;
import com.lebo.service.TimelineParam;
import com.lebo.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.mapper.BeanMapper;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-3
 * Time: PM8:35
 */
@Controller
@RequestMapping(value = "/api/v1/statuses")
public class StatusRestController {
    @Autowired
    private StatusService statusService;

    @Autowired
    private AccountService accountService;

    private Logger logger = LoggerFactory.getLogger(StatusRestController.class);

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@RequestParam(value = "video") MultipartFile video,
                         @RequestParam(value = "image") MultipartFile image,
                         @RequestParam(value = "text") String text) {
        try {

            List<StatusService.File> files = Arrays.asList(
                    new StatusService.File(video.getInputStream(), video.getOriginalFilename(), video.getContentType()),
                    new StatusService.File(image.getInputStream(), image.getOriginalFilename(), image.getContentType())
            );

            return statusService.update(accountService.getCurrentUserId(), text, files);

        } catch (DuplicateException e) {
            return ErrorDto.DUPLICATE;
        } catch (Exception e) {
            logger.info("发布Tweet失败", e);
            return ErrorDto.newInternalServerError(e.getMessage());
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
    @RequestMapping(value = "home_timeline", method = RequestMethod.GET)
    public Object homeTimeline() {
        return null;
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
        List<Tweet> tweetList = statusService.userTimeline(param);

        List<StatusDto> dtoList = Lists.newArrayList();
        for (Tweet tweet : tweetList) {
            StatusDto dto = BeanMapper.map(tweet, StatusDto.class);
            dto.setUser(accountService.getUser(tweet.getUserId()));
            dtoList.add(dto);
        }

        return dtoList;
    }
}
