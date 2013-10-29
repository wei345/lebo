package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 即时通讯.
 *
 * @author: Wei Liu
 * Date: 13-10-14
 * Time: AM11:17
 */
@Controller
@RequestMapping("/api/1/im")
public class ImRestController {
    @Autowired
    private AccountService accountService;

    @RequestMapping("profiles")   //为确保不受客户端或服务器url长度限制，也允许post
    @ResponseBody
    public Object profiles(@RequestParam("userIds") String userIds) {
        String[] ids = userIds.split("\\s*,\\s*");
        List<User> users = new ArrayList<User>(ids.length);
        for (int i = 0; i < ids.length; i++) {
            users.add(accountService.getUser(ids[i]));
        }

        return accountService.toBasicUserDtos(users);
    }

    @RequestMapping(value = "completeUpload", method = RequestMethod.POST)
    @ResponseBody
    public Object completeUpload(@RequestParam("fromUserId") String fromUserId,
                                 @RequestParam("toUserId") String toUserId,
                                 @RequestParam("attachmentUrl") String attachmentUrl){



        return null;

    }
}
