package com.lebo.rest;

import com.lebo.entity.Following;
import com.lebo.entity.User;
import com.lebo.service.FriendshipService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PaginationParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户关注的人。
 *
 * @author: Wei Liu
 * Date: 13-7-22
 * Time: PM7:15
 */
@Controller
@RequestMapping("/api/1/friends")
public class FriendRestController {
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "screenName", required = false) String screenName,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = PaginationParam.DEFAULT_COUNT + "") int size) {
        if(StringUtils.isBlank(userId) && StringUtils.isBlank(screenName)){
            userId = accountService.getCurrentUserId();
        } else {
            userId = accountService.getUserId(userId, screenName);
        }

        List<Following> followings = friendshipService.getFriends(userId, new PageRequest(page, size, PaginationParam.DEFAULT_SORT));

        List<User> users = new ArrayList<User>();
        for (Following following : followings) {
            users.add(accountService.getUser(following.getFollowingId()));
        }

        return accountService.toUserDtos(users);
    }
}
