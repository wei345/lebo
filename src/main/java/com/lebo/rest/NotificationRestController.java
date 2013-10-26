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
import java.util.Arrays;
import java.util.List;

/**
 * 消息(通知，UI显示"消息")。
 *
 * @author: Wei Liu
 * Date: 13-8-7
 * Time: PM6:30
 */
@Controller
public class NotificationRestController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AccountService accountService;

    public static final String PREFIX_API_1 = "/api/1/notifications/";
    public static final String PREFIX_API_1_1 = "/api/1.1/notifications/";

    @RequestMapping(value = PREFIX_API_1 + "list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@Valid PaginationParam param,
                       @RequestParam(value = "unread", required = false) Boolean unread) {
        List<Notification> notifications;

        List<String> activityTypes = Arrays.asList(
                Notification.ACTIVITY_TYPE_REPOST,
                Notification.ACTIVITY_TYPE_REPLY_POST,
                Notification.ACTIVITY_TYPE_POST_AT,

                Notification.ACTIVITY_TYPE_REPLY_COMMENT,
                Notification.ACTIVITY_TYPE_COMMENT_AT,

                Notification.ACTIVITY_TYPE_FAVORITE,
                Notification.ACTIVITY_TYPE_FOLLOW
        );

        notifications = notificationService.find(accountService.getCurrentUserId(), unread, activityTypes, param);

        notificationService.markRead(notifications);
        return notificationService.toNotificationDtos(notifications);
    }

    /**
     * 标记所有未读通知为已读。
     */
    @RequestMapping(value = PREFIX_API_1 + "markAllRead", method = RequestMethod.POST)
    @ResponseBody
    public void markAllRead() {
        notificationService.markAllRead(accountService.getCurrentUserId());
    }

    @RequestMapping(value = PREFIX_API_1_1 + "list.json", method = RequestMethod.GET)
    @ResponseBody
    public Object list_v1_1(@Valid PaginationParam param,
                            @RequestParam(value = "unread", required = false) Boolean unread,
                            @RequestParam(value = "types", required = false) String types) {

        List<String> activityTypes = null;
        if (types != null) {
            activityTypes = Arrays.asList(types.split("\\s*,\\s*"));
        }

        List<Notification> notifications;
        notifications = notificationService.find(accountService.getCurrentUserId(), unread, activityTypes, param);

        notificationService.markRead(notifications);

        return notificationService.toNotificationDtos(notifications);
    }
}
