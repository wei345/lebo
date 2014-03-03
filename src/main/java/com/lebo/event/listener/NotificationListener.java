package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.entity.*;
import com.lebo.event.*;
import com.lebo.jms.ApnsMessageProducer;
import com.lebo.service.*;
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
    @Autowired
    private BlockService blockService;
    @Autowired
    private SettingService settingService;

    private Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    /**
     * 用户被关注时，通知被关注的用户。
     */
    @Subscribe
    public void afterFollowingCreate(AfterFollowingCreatEvent event) {
        Notification notification = createNotification(Notification.ACTIVITY_TYPE_FOLLOW,
                event.getFollowingId(), event.getUserId(), null, null, null);

        sendNotificationQueue(notification, null);
    }

    /**
     * 帖子被收藏(喜欢)时，通知帖子作者。
     */
    @Subscribe
    public void afterFavoriteCreate(AfterFavoriteCreateEvent event) {
        if (!event.getFavorite().getPostUserId().equals(event.getFavorite().getUserId())) {//自己收藏自己的帖子，不发通知
            Notification notification = createNotification(Notification.ACTIVITY_TYPE_FAVORITE,
                    event.getFavorite().getPostUserId(), event.getFavorite().getUserId(),
                    Notification.OBJECT_TYPE_POST, event.getFavorite().getPostId(), null);

            sendNotificationQueue(notification, null);
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
            //加精，赠送玫瑰并发通知
            if (event.getPost().getUserId().equals(settingService.getSetting().getDigestAccountId())) {// 加精

                vgService.giveGoodsWhenDigest(event.getPost().getOriginPostUserId(), event.getPost().getOriginPostId());

                Notification notification = createNotification(
                        Notification.ACTIVITY_TYPE_DIGEST,
                        event.getPost().getOriginPostUserId(),
                        event.getPost().getUserId(),
                        Notification.OBJECT_TYPE_POST,
                        event.getPost().getOriginPostId(),
                        "恭喜你, 你导演的视频被加为精华!获得1朵玫瑰(价值10金币)奖励!快去分享给你的小伙伴们吧!(点击系统消息,会出现“分享”按钮,点击后可以将该视频分享到微信朋友圈、微信、QQ、新浪微博、人人网、QQ 空间等平台)");

                sendNotificationQueue(notification, notification.getText());

            }
            //发送转发通知
            else if (!event.getPost().getOriginPostUserId().equals(event.getPost().getUserId())) { //非转发自己的帖子
                Notification notification = createNotification(Notification.ACTIVITY_TYPE_REPOST,
                        event.getPost().getOriginPostUserId(), event.getPost().getUserId(),
                        Notification.OBJECT_TYPE_POST, event.getPost().getOriginPostId(), null);

                sendNotificationQueue(notification, null);
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
                    //用户被黑名单中的人at，不发通知
                    if (blockService.isBlocking(userId, event.getPost().getUserId())) {
                        continue;
                    }

                    Notification notification = createNotification(Notification.ACTIVITY_TYPE_POST_AT,
                            userId, event.getPost().getUserId(),
                            Notification.OBJECT_TYPE_POST, event.getPost().getId(), null);

                    sendNotificationQueue(notification, null);
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
                        Notification.OBJECT_TYPE_COMMENT, event.getComment().getId(), null);

                sendNotificationQueue(notification, null);
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
                    Notification.OBJECT_TYPE_COMMENT, event.getComment().getId(), null);

            sendNotificationQueue(notification, null);
        }

        //comment中at
        for (String userId : event.getComment().getMentionUserIds()) {
            //自己at自己，不发通知
            if (event.getComment().getUserId().equals(userId)) {
                continue;
            }
            //用户被黑名单中的人at，不发通知
            if (blockService.isBlocking(userId, event.getComment().getUserId())) {
                continue;
            }

            Notification notification = createNotification(Notification.ACTIVITY_TYPE_COMMENT_AT,
                    userId, event.getComment().getUserId(),
                    Notification.OBJECT_TYPE_COMMENT, event.getComment().getId(), null);

            sendNotificationQueue(notification, null);
        }
    }

    @Subscribe
    public void afterGiveGoods(GiveGoodsNotificationEvent event) {

        GiveGoods giveGoods = event.getGiveGoods();

        User fromUser = accountService.getUser(giveGoods.getFromUserId());
        Goods goods = vgService.getGoodsById(giveGoods.getGoodsId());

        Notification notification = createNotification(
                Notification.ACTIVITY_TYPE_GIVE_GOODS,
                giveGoods.getToUserId(),
                giveGoods.getFromUserId(),
                Notification.OBJECT_TYPE_POST,
                giveGoods.getPostId(),
                String.format("送了您%s%s%s",
                        giveGoods.getQuantity(),
                        goods.getQuantityUnit(),
                        goods.getName()));

        sendNotificationQueue(notification, fromUser.getScreenName() + " " + notification.getText());
    }

    private void sendNotificationQueue(Notification notification, String msg) {
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
            if (StringUtils.isNotBlank(msg)) {
                message = msg;
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
                                            String objectType, String objectId, String text) {
        Notification notification = new Notification();
        notification.setActivityType(activityType);
        notification.setCreatedAt(new Date());
        notification.setRecipientId(recipientId);
        notification.setUnread(true);
        notification.setSenderId(senderId);
        notification.setObjectType(objectType);
        notification.setObjectId(objectId);
        notification.setText(text);
        return notificationService.create(notification);
    }
}
