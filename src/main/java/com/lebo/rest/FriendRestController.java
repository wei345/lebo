package com.lebo.rest;

import com.lebo.service.FriendshipService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
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
                       @Valid PageRequest pageRequest) {
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(screenName)) {
            userId = accountService.getCurrentUserId();
        } else {
            userId = accountService.getUserId(userId, screenName);
        }

        List<String> ids = friendshipService.getFriends(userId, pageRequest);
        return accountService.toUserDtos(accountService.getUsers(ids));
    }

    /**
     * 双向关注。
     */
    @RequestMapping(value = "bilateral", method = RequestMethod.GET)
    @ResponseBody
    public Object bilateral(@RequestParam(value = "userId", required = false) String userId,
                            @RequestParam(value = "screenName", required = false) String screenName,
                            @Valid PageRequest pageRequest) {
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(screenName)) {
            userId = accountService.getCurrentUserId();
        } else {
            userId = accountService.getUserId(userId, screenName);
        }

        List<String> ids = friendshipService.getBilateralFriends(userId, pageRequest);
        return accountService.toUserDtos(accountService.getUsers(ids));
    }
}
