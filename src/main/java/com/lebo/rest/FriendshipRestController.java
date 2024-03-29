package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
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

/**
 * Friends & Followers
 *
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: PM4:17
 */
@Controller
@RequestMapping("/api/1/friendships")
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
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@RequestParam(value = "userId", required = false) String userId,
                         @RequestParam(value = "screenName", required = false) String screenName,
                         @RequestParam(value = "follow", required = false) String follow) {
        try {
            userId = accountService.getUserId(userId, screenName);
            friendshipService.follow(accountService.getCurrentUserId(), userId);
        } catch (DuplicateException e) {
            return ErrorDto.duplicate();
        } catch (ServiceException e) {
            return ErrorDto.badRequest(e);
        }

        User user = accountService.getUser(userId);
        return accountService.toUserDto(user);
    }

    /**
     * 取消关注
     *
     * @param userId
     * @param screenName
     * @return
     */
    @RequestMapping(value = "destroy", method = RequestMethod.POST)
    @ResponseBody
    public Object destroy(@RequestParam(value = "userId", required = false) String userId,
                          @RequestParam(value = "screenName", required = false) String screenName) {
        userId = accountService.getUserId(userId, screenName);

        try {
            friendshipService.unfollow(accountService.getCurrentUserId(), userId);
        } catch (ServiceException e) {
            return ErrorDto.badRequest(e.getMessage());
        }

        User user = accountService.getUser(userId);
        return accountService.toUserDto(user);
    }
}
