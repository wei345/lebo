package com.lebo.rest;

import com.lebo.Constants;
import com.lebo.entity.Notification;
import com.lebo.rest.dto.NotificationGroupDto;
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
import java.util.ArrayList;
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
        notificationService.markAllRead(accountService.getCurrentUserId(), Arrays.asList(
                Notification.ACTIVITY_TYPE_REPOST,
                Notification.ACTIVITY_TYPE_REPLY_POST,
                Notification.ACTIVITY_TYPE_POST_AT,

                Notification.ACTIVITY_TYPE_REPLY_COMMENT,
                Notification.ACTIVITY_TYPE_COMMENT_AT,

                Notification.ACTIVITY_TYPE_FAVORITE,
                Notification.ACTIVITY_TYPE_FOLLOW
        ));
    }

    //---- 1.1 ----//

    @RequestMapping(value = PREFIX_API_1_1 + "list.json", method = RequestMethod.GET)
    @ResponseBody
    public Object list_v1_1(@Valid PaginationParam param,
                            @RequestParam(value = "unread", required = false) Boolean unread,
                            @RequestParam(value = "types", required = false) String types) {

        List<String> activityTypes = null;
        if (types != null) {
            activityTypes = Arrays.asList(types.split(Constants.COMMA_SEPARATOR));
        }

        List<Notification> notifications;
        notifications = notificationService.find(accountService.getCurrentUserId(), unread, activityTypes, param);

        notificationService.markRead(notifications);

        return notificationService.toNotificationDtos(notifications);
    }

    @RequestMapping(value = PREFIX_API_1_1 + "groups.json", method = RequestMethod.GET)
    @ResponseBody
    public Object groups() {
        List<NotificationGroupDto> dtos = new ArrayList<NotificationGroupDto>(2);

        //
        NotificationGroupDto leboTeamGroup = new NotificationGroupDto();

        leboTeamGroup.setActivityTypes(Arrays.asList(Notification.ACTIVITY_TYPE_LEBO_TEAM));

        leboTeamGroup.setGroupName("通知");

        leboTeamGroup.setUnreadCount(notificationService.count(
                accountService.getCurrentUserId(), true, leboTeamGroup.getActivityTypes()));

        leboTeamGroup.setRecentNotifications(
                notificationService.toNotificationDtos(
                        notificationService.find(
                                accountService.getCurrentUserId(), leboTeamGroup.getActivityTypes(), 1)));

        dtos.add(leboTeamGroup);

        //
        NotificationGroupDto otherGroup = new NotificationGroupDto();

        otherGroup.setActivityTypes(Arrays.asList(
                Notification.ACTIVITY_TYPE_REPOST,
                Notification.ACTIVITY_TYPE_REPLY_POST,
                Notification.ACTIVITY_TYPE_POST_AT,

                Notification.ACTIVITY_TYPE_REPLY_COMMENT,
                Notification.ACTIVITY_TYPE_COMMENT_AT,

                Notification.ACTIVITY_TYPE_FAVORITE,
                Notification.ACTIVITY_TYPE_FOLLOW
        ));

        otherGroup.setGroupName("互动");

        otherGroup.setUnreadCount(notificationService.count(
                accountService.getCurrentUserId(), true, otherGroup.getActivityTypes()));

        otherGroup.setRecentNotifications(
                notificationService.toNotificationDtos(
                        notificationService.find(
                                accountService.getCurrentUserId(), otherGroup.getActivityTypes(), 1)));

        dtos.add(otherGroup);

        return dtos;
    }
}
