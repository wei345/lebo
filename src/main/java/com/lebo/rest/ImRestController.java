package com.lebo.rest;

import com.lebo.entity.Im;
import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.ImService;
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
public class ImRestController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ImService imService;

    public static final String PREFIX_API_1_1_IM = "/api/1.1/ims/";

    private static int RECENT_MAX_COUNT = 1000;

    @RequestMapping(PREFIX_API_1_1_IM + "profiles.json")   //为确保不受客户端或服务器url长度限制，也允许post
    @ResponseBody
    public Object profiles(@RequestParam("userIds") String userIds) {
        String[] ids = userIds.split("\\s*,\\s*");
        List<User> users = new ArrayList<User>(ids.length);
        for (int i = 0; i < ids.length; i++) {
            User user = accountService.getUser(ids[i]);
            if (user == null) {
                return ErrorDto.badRequest("用户不存在, id: [" + ids[i] + "]");
            }
            users.add(user);
        }

        return accountService.toBasicUserDtos(users);
    }

    @RequestMapping(value = PREFIX_API_1_1_IM + "new.json", method = RequestMethod.POST)
    @ResponseBody
    public Object newMessage(@RequestParam("toUserId") String toUserId,
                             @RequestParam(value = "message") String message,
                             @RequestParam(value = "messageTime") long messageTime,
                             @RequestParam("type") int type) {

        if (!accountService.isUserExists(toUserId)) {
            return ErrorDto.badRequest("toUserId[" + toUserId + "]用户不存在");
        }

        Im im = imService.newMessage(accountService.getCurrentUserId(), toUserId, message, type, messageTime);

        return imService.toDto(im);
    }

    @RequestMapping(value = PREFIX_API_1_1_IM + "recent.json", method = RequestMethod.GET)
    @ResponseBody
    public Object recentMessage(@RequestParam("afterTime") long afterTime) {
        return imService.toDtos(
                imService.getRecentMessage(
                        accountService.getCurrentUserId(),
                        afterTime,
                        RECENT_MAX_COUNT));
    }
}
