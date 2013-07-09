package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.UserDto;
import com.lebo.service.DuplicateException;
import com.lebo.service.FriendshipService;
import com.lebo.service.ServiceException;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.BeanMapper;

/**
 * Friends & Followers
 *
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: PM4:17
 */
@Controller
@RequestMapping("/api/v1/friendships")
public class FriendshipRestController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private AccountService accountService;

    /**
     * <h2>Twitter</h2>
     * <p>
     * Allows the authenticating users to follow the user specified in the ID parameter.
     * </p>
     * <p>
     * Returns the befriended user in the requested format when successful. Returns a string
     * describing the failure condition when unsuccessful. If you are already friends with
     * the user a HTTP 403 may be returned, though for performance reasons you may get a
     * 200 OK message even if the friendship already exists.
     * </p>
     * <p>
     * Actions taken in this method are asynchronous and changes will be eventually consistent.
     * </p>
     * <p/>
     * Providing either screen_name or user_id is required.
     * <p/>
     * https://dev.twitter.com/docs/api/1.1/post/friendships/create
     *
     * @param userId     The ID of the user for whom to befriend.
     * @param screenName The screen name of the user for whom to befriend
     * @param follow     Enable notifications for the target user.
     */
    //TODO 发送通知给被follow的用户
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@RequestParam(value = "userId", required = false) String userId,
                         @RequestParam(value = "screenName", required = false) String screenName,
                         @RequestParam(value = "follow", required = false) String follow) {
        try {
            userId = accountService.getUserId(userId, screenName);
            friendshipService.follow(accountService.getCurrentUserId(), userId);
        } catch (DuplicateException e) {
            return ErrorDto.DUPLICATE;
        } catch (ServiceException e) {
            return ErrorDto.newBadRequestError(e.getMessage());
        }

        User user = accountService.getUser(userId);
        UserDto dto = BeanMapper.map(user, UserDto.class);
        dto.setFollowing(true);
        return dto;
    }

    /**
     * <h2>Twitter</h2>
     * <p>
     * Allows the authenticating user to unfollow the user specified in the ID parameter.
     * </p>
     * <p>
     * Returns the unfollowed user in the requested format when successful. Returns a string
     * describing the failure condition when unsuccessful.
     * </p>
     * <p>
     * Actions taken in this method are asynchronous and changes will be eventually consistent.
     * </p>
     * <p>
     * https://dev.twitter.com/docs/api/1.1/post/friendships/destroy
     * </p>
     */
    @RequestMapping(value = "destroy", method = RequestMethod.POST)
    @ResponseBody
    public Object destroy(@RequestParam(value = "userId", required = false) String userId,
                          @RequestParam(value = "screenName", required = false) String screenName) {
        userId = accountService.getUserId(userId, screenName);

        try {
            friendshipService.unfollow(accountService.getCurrentUserId(), userId);
        } catch (ServiceException e) {
            return ErrorDto.newBadRequestError(e.getMessage());
        }

        User user = accountService.getUser(userId);
        return BeanMapper.map(user, UserDto.class);
    }
}
