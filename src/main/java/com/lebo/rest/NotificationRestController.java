package com.lebo.rest;

import com.lebo.entity.Notification;
import com.lebo.service.NotificationService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PaginationParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * 消息(通知，UI显示"消息")。
 *
 * @author: Wei Liu
 * Date: 13-8-7
 * Time: PM6:30
 */
@Controller
@RequestMapping("/api/1/notifications")
public class NotificationRestController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@Valid PaginationParam param, @RequestParam(value = "unread", required = false) Boolean unread) {
        List<Notification> notifications;
        //在全部通知中查找，不区分是否未读
        if (unread == null) {
            notifications = notificationService.find(accountService.getCurrentUserId(), param);
        }
        //只查找未读的通知
        else {
            notifications = notificationService.findUnread(accountService.getCurrentUserId(), param);
        }
        notificationService.markRead(notifications);
        return notificationService.toNotificationDtos(notifications);
    }

    /**
     * 标记所有未读通知为已读。
     */
    @RequestMapping(value = "markAllRead", method = RequestMethod.POST)
    @ResponseBody
    public void markAllRead() {
        notificationService.markAllRead(accountService.getCurrentUserId());
    }
}
