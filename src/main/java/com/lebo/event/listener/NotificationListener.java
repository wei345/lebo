package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.entity.Notification;
import com.lebo.entity.Post;
import com.lebo.entity.User;
import com.lebo.event.AfterCommentCreateEvent;
import com.lebo.event.AfterFavoriteCreateEvent;
import com.lebo.event.AfterFollowingCreatEvent;
import com.lebo.event.AfterPostCreateEvent;
import com.lebo.jms.ApnsMessageProducer;
import com.lebo.service.NotificationService;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 当用户被关注、喜欢、转播、at、回复帖子、回复评论时，通知用户。
 *
 * @author: Wei Liu
 * Date: 13-8-9
 * Time: AM10:44
 */
@Component
public class NotificationListener {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ApnsMessageProducer apnsMessageProducer;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StatusService statusService;

    /**
     * 用户被关注时，通知被关注的用户。
     */
    @Subscribe
    public void afterFollowingCreate(AfterFollowingCreatEvent event) {
        Notification notification = createNotification(Notification.ACTIVITY_TYPE_FOLLOW,
                event.getFollowingId(), event.getUserId(), null, null);

        sendNotificationQueue(notification, "%s 关注了你");
    }

    /**
     * 帖子被收藏(喜欢)时，通知帖子作者。
     */
    @Subscribe
    public void afterFavoriteCreate(AfterFavoriteCreateEvent event) {
        Notification notification = createNotification(Notification.ACTIVITY_TYPE_FAVORITE,
                event.getFavorite().getPostUserId(), event.getFavorite().getUserId(),
                Notification.OBJECT_TYPE_POST, event.getFavorite().getPostId());

        sendNotificationQueue(notification, "%s 喜欢你的视频");
    }

    /**
     * 帖子中提到用户，通知被提到的用户。
     * 帖子被转发，通知帖子作者。
     */
    @Subscribe
    public void afterPostCreate(AfterPostCreateEvent event) {
        //转发
        if (event.getPost().getOriginPostId() != null) {
            Notification notification = createNotification(Notification.ACTIVITY_TYPE_REPOST,
                    event.getPost().getOriginPostUserId(), event.getPost().getUserId(),
                    Notification.OBJECT_TYPE_POST, event.getPost().getOriginPostId());

            sendNotificationQueue(notification, "%s 转播了你的视频");
        }
        //原帖，at通知
        else {
            for (String userId : event.getPost().getUserMentions()) {
                //自己at自己，不发at通知
                if (event.getPost().getUserId().equals(userId)) {
                    continue;
                }
                Notification notification = createNotification(Notification.ACTIVITY_TYPE_POST_AT,
                        userId, event.getPost().getUserId(),
                        Notification.OBJECT_TYPE_POST, event.getPost().getId());

                sendNotificationQueue(notification, "%s at了你");
            }
        }
    }

    /**
     * 帖子被回复，通知帖子作者。
     * 评论被回复，通知评论作者。
     * 在评论中提到用户，通知被提到的用户。
     */
    @Subscribe
    public void afterCommentCreate(AfterCommentCreateEvent event) {
        //回复comment
        if (event.getComment().getReplyCommentId() != null) {
            Notification notification = createNotification(Notification.ACTIVITY_TYPE_REPLY_COMMENT,
                    event.getComment().getReplyCommentUserId(), event.getComment().getUserId(),
                    Notification.OBJECT_TYPE_COMMENT, event.getComment().getId());

            sendNotificationQueue(notification, "%s 回复了你的评论");
        }
        //回复post
        Post post = statusService.getPost(event.getComment().getPostId());
        Notification notification = createNotification(Notification.ACTIVITY_TYPE_REPLY_POST,
                post.getUserId(), event.getComment().getUserId(),
                Notification.OBJECT_TYPE_COMMENT, event.getComment().getId());

        sendNotificationQueue(notification, "%s 回复了你的视频");

        //comment中at
        for (String userId : event.getComment().getMentions()) {
            //自己at自己，不发at通知
            if (event.getComment().getUserId().equals(userId)) {
                continue;
            }
            notification = createNotification(Notification.ACTIVITY_TYPE_COMMENT_AT,
                    userId, event.getComment().getUserId(),
                    Notification.OBJECT_TYPE_COMMENT, event.getComment().getId());

            sendNotificationQueue(notification, "%s at了你");
        }
    }

    private void sendNotificationQueue(Notification notification, String messageFormat) {
        //自己对自己的操作，不发送通知
        if(notification.getSenderId().equals(notification.getRecipientId())){
            return;
        }

        User sender = accountService.getUser(notification.getSenderId());
        User recipient = accountService.getUser(notification.getRecipientId());
        if (StringUtils.isNotBlank(recipient.getApnsProductionToken())) {
            apnsMessageProducer.sendNotificationQueue(String.format(messageFormat, sender.getScreenName()),
                    recipient.getApnsProductionToken(), recipient.getId());
        }
    }

    private Notification createNotification(String activityType, String recipientId, String senderId,
                                            String objectType, String objectId) {
        Notification notification = new Notification();
        notification.setActivityType(activityType);
        notification.setCreatedAt(new Date());
        notification.setRecipientId(recipientId);
        notification.setUnread(true);
        notification.setSenderId(senderId);
        notification.setObjectType(objectType);
        notification.setObjectId(objectId);
        return notificationService.create(notification);
    }
}
