package com.lebo.rest;

import com.lebo.entity.FileInfo;
import com.lebo.entity.Im;
import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.ALiYunStorageService;
import com.lebo.service.ImService;
import com.lebo.service.ServiceException;
import com.lebo.service.account.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
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
    private ALiYunStorageService aLiYunStorageService;
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
            users.add(accountService.getUser(ids[i]));
        }

        return accountService.toBasicUserDtos(users);
    }

    @RequestMapping(value = PREFIX_API_1_1_IM + "new.json", method = RequestMethod.POST)
    @ResponseBody
    public Object newMessage(@RequestParam("fromUserId") String fromUserId,
                             @RequestParam("toUserId") String toUserId,
                             @RequestParam(value = "message", required = false) String message,
                             @RequestParam(value = "attachmentUrl", required = false) String[] attachmentUrls) {

        if ((attachmentUrls == null || attachmentUrls.length == 0) && StringUtils.isBlank(message)) {
            return ErrorDto.badRequest("message和attachmentUrl不能都为空");
        }

        List<FileInfo> fileInfos = new ArrayList<FileInfo>(attachmentUrls.length);
        try {
            for (String attachmentUrl : attachmentUrls) {
                fileInfos.add(aLiYunStorageService.getTmpFileInfoFromUrl(attachmentUrl));
            }
        } catch (ServiceException e) {
            return ErrorDto.badRequest(e.getMessage());
        }

        Im im = imService.create(fromUserId, toUserId, message, fileInfos);

        return imService.toDto(im);
    }

    @RequestMapping(value = PREFIX_API_1_1_IM + "recent.json", method = RequestMethod.GET)
    @ResponseBody
    public Object recentMessage(@RequestParam("fromTime") long fromTime) {
        return imService.toDtos(imService.getRecentMessage(new Date(fromTime * 1000), RECENT_MAX_COUNT));
    }
}
