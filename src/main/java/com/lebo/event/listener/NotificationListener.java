package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.entity.*;
import com.lebo.event.*;
import com.lebo.jms.ApnsMessageProducer;
import com.lebo.service.NotificationService;
import com.lebo.service.StatusService;
import com.lebo.service.VgService;
import com.lebo.service.account.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private VgService vgService;

    private Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    /**
     * 用户被关注时，通知被关注的用户。
     */
    @Subscribe
    public void afterFollowingCreate(AfterFollowingCreatEvent event) {
        Notification notification = createNotification(Notification.ACTIVITY_TYPE_FOLLOW,
                event.getFollowingId(), event.getUserId(), null, null);

        sendNotificationQueue(notification);
    }

    /**
     * 帖子被收藏(喜欢)时，通知帖子作者。
     */
    @Subscribe
    public void afterFavoriteCreate(AfterFavoriteCreateEvent event) {
        if (!event.getFavorite().getPostUserId().equals(event.getFavorite().getUserId())) {//自己收藏自己的帖子，不发通知
            Notification notification = createNotification(Notification.ACTIVITY_TYPE_FAVORITE,
                    event.getFavorite().getPostUserId(), event.getFavorite().getUserId(),
                    Notification.OBJECT_TYPE_POST, event.getFavorite().getPostId());

            sendNotificationQueue(notification);
        }
    }

    /**
     * 帖子中提到用户，通知被提到的用户。
     * 帖子被转发，通知帖子作者。
     */
    @Subscribe
    public void afterPostCreate(AfterPostCreateEvent event) {
        //转发
        if (event.getPost().getOriginPostId() != null) {
            if (!event.getPost().getOriginPostUserId().equals(event.getPost().getUserId())) { //转发自己的帖子不发通知
                Notification notification = createNotification(Notification.ACTIVITY_TYPE_REPOST,
                        event.getPost().getOriginPostUserId(), event.getPost().getUserId(),
                        Notification.OBJECT_TYPE_POST, event.getPost().getOriginPostId());

                sendNotificationQueue(notification);
            }
        }
        //原帖，at通知
        else {
            if (event.getPost().isPublic()) {
                for (String userId : event.getPost().getMentionUserIds()) {
                    //自己at自己，不发at通知
                    if (event.getPost().getUserId().equals(userId)) {
                        continue;
                    }
                    Notification notification = createNotification(Notification.ACTIVITY_TYPE_POST_AT,
                            userId, event.getPost().getUserId(),
                            Notification.OBJECT_TYPE_POST, event.getPost().getId());

                    sendNotificationQueue(notification);
                }
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
            if (event.getComment().getUserId().equals(event.getComment().getReplyCommentUserId())) {
                //回复自己的评论，不发通知
            } else {
                Notification notification = createNotification(Notification.ACTIVITY_TYPE_REPLY_COMMENT,
                        event.getComment().getReplyCommentUserId(), event.getComment().getUserId(),
                        Notification.OBJECT_TYPE_COMMENT, event.getComment().getId());

                sendNotificationQueue(notification);
            }
        }

        //回复post
        Post post = statusService.getPost(event.getComment().getPostId());
        if (event.getComment().getReplyCommentUserId() != null
                && event.getComment().getReplyCommentUserId().equals(post.getUserId())) {

            //被回复的评论作者和帖子作者是同一个人，不发回复帖子通知

        } else if (post.getUserId().equals(event.getComment().getUserId())) {

            //回复自己帖子不通知自己

        } else {
            Notification notification = createNotification(Notification.ACTIVITY_TYPE_REPLY_POST,
                    post.getUserId(), event.getComment().getUserId(),
                    Notification.OBJECT_TYPE_COMMENT, event.getComment().getId());

            sendNotificationQueue(notification);
        }

        //comment中at
        for (String userId : event.getComment().getMentionUserIds()) {
            //自己at自己，不发通知
            if (event.getComment().getUserId().equals(userId)) {
                continue;
            }
            Notification notification = createNotification(Notification.ACTIVITY_TYPE_COMMENT_AT,
                    userId, event.getComment().getUserId(),
                    Notification.OBJECT_TYPE_COMMENT, event.getComment().getId());

            sendNotificationQueue(notification);
        }
    }

    @Subscribe
    public void afterGiveGoods(AfterGiveGoodsEvent event) {

        GiveGoods giveGoods = event.getGiveGoods();

        Notification notification = createNotification(
                Notification.ACTIVITY_TYPE_GIVE_GOODS,
                giveGoods.getToUserId(),
                giveGoods.getFromUserId(),
                Notification.OBJECT_TYPE_POST,
                giveGoods.getPostId());

        User fromUser = accountService.getUser(giveGoods.getFromUserId());
        Goods goods = vgService.getGoodsById(giveGoods.getGoodsId());

        notification.setText(
                String.format("%s 送了您 %s %s %s",
                        fromUser.getScreenName(),
                        giveGoods.getQuantity(),
                        goods.getQuantityUnit(),
                        goods.getName()));

        sendNotificationQueue(notification);
    }

    private void sendNotificationQueue(Notification notification) {
        User recipient = accountService.getUser(notification.getRecipientId());
        //如果用户设置不接收，则不发推送通知
        if (notification.getActivityType().equals(Notification.ACTIVITY_TYPE_FOLLOW)) {
            if (recipient.getNotifyOnFollow() != null && !recipient.getNotifyOnFollow()) {
                logger.debug("不发送APNS通知: {}({}) 已设置不接收被关注通知", recipient.getScreenName(), recipient.getId());
                return;
            }
        }
        //如果用户设置不接收，则不发推送通知
        if (notification.getActivityType().equals(Notification.ACTIVITY_TYPE_FAVORITE)) {
            if (recipient.getNotifyOnFavorite() != null && !recipient.getNotifyOnFavorite()) {
                logger.debug("不发送APNS通知: {}({}) 已设置不接收被喜欢通知", recipient.getScreenName(), recipient.getId());
                return;
            }
        }
        //如果用户设置不接收，则不发推送通知
        if (notification.getActivityType().equals(Notification.ACTIVITY_TYPE_REPLY_POST)) {
            if (recipient.getNotifyOnReplyPost() != null && !recipient.getNotifyOnReplyPost()) {
                logger.debug("不发送APNS通知: {}({}) 已设置不接收被回复通知", recipient.getScreenName(), recipient.getId());
                return;
            }
        }

        User sender = accountService.getUser(notification.getSenderId());

        if (StringUtils.isNotBlank(recipient.getApnsProductionToken())) {

            String message = "";
            if (StringUtils.isNotBlank(notification.getText())) {
                message = notification.getText();
            } else if (Notification.ACTIVITY_TYPE_FOLLOW.equals(notification.getActivityType())) {
                message = String.format("%s 关注了你", sender.getScreenName());
            } else if (Notification.ACTIVITY_TYPE_FAVORITE.equals(notification.getActivityType())) {
                message = String.format("%s 喜欢你的视频", sender.getScreenName());
            } else if (Notification.ACTIVITY_TYPE_REPOST.equals(notification.getActivityType())) {
                message = String.format("%s 转播了你的视频", sender.getScreenName());
            } else if (Notification.ACTIVITY_TYPE_POST_AT.equals(notification.getActivityType())) {
                message = String.format("%s @了你", sender.getScreenName());
            } else if (Notification.ACTIVITY_TYPE_REPLY_COMMENT.equals(notification.getActivityType())) {
                message = String.format("%s 回复了你的评论", sender.getScreenName());
            } else if (Notification.ACTIVITY_TYPE_REPLY_POST.equals(notification.getActivityType())) {
                message = String.format("%s 回复了你的视频", sender.getScreenName());
            } else if (Notification.ACTIVITY_TYPE_COMMENT_AT.equals(notification.getActivityType())) {
                message = String.format("%s @了你", sender.getScreenName());
            }

            apnsMessageProducer.sendNotificationQueue(message, recipient.getApnsProductionToken(), recipient);
        } else {
            logger.debug("不发送APNS通知: {}({}) 没有APNS token", recipient.getScreenName(), recipient.getId());
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
