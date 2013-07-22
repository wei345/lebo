package com.lebo.rest;

import com.lebo.entity.Following;
import com.lebo.entity.User;
import com.lebo.service.FriendshipService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PaginationParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户的粉丝。
 *
 * @author: Wei Liu
 * Date: 13-7-22
 * Time: PM7:21
 */
@Controller
@RequestMapping("/api/1/followers")
public class FollowerRestController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Object showFans(@RequestParam(value = "userId") String userId,
                           @Valid PaginationParam param) {

        List<Following> followers = friendshipService.getFollowers(userId, param);
        List<User> users = new ArrayList<User>();
        for (Following following : followers) {
            users.add(accountService.getUser(following.getUserId()));
        }

        return accountService.toUserDtos(users);
    }
}
