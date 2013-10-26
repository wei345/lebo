package com.lebo.entity;

import com.lebo.service.FileContentUrlUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 通知(UI显示为消息)
 *
 * @author: Wei Liu
 * Date: 13-8-8
 * Time: PM5:57
 */
@Document(collection = "notifications")
public class Notification extends IdEntity {
    public static final String ACTIVITY_TYPE_FOLLOW = "follow";
    public static final String ACTIVITY_TYPE_FAVORITE = "favorite";
    public static final String ACTIVITY_TYPE_REPOST = "repost";
    public static final String ACTIVITY_TYPE_POST_AT = "post_at";
    public static final String ACTIVITY_TYPE_COMMENT_AT = "comment_at";
    public static final String ACTIVITY_TYPE_REPLY_POST = "reply_post";
    public static final String ACTIVITY_TYPE_REPLY_COMMENT = "reply_comment";
    public static final String ACTIVITY_TYPE_SYSTEM = "system";
    public static final String OBJECT_TYPE_POST = "post";
    public static final String OBJECT_TYPE_COMMENT = "comment";

    //接收通知的用户ID
    @Indexed
    private String recipientId;
    public static final String RECIPIENT_ID_KEY = "recipientId";
    private Date createdAt;
    private Boolean unread;
    public static final String UNREAD_KEY = "unread";
    //触发通知的用户ID
    private String senderId;
    //关注、喜欢、转播、at、评论、评论的评论
    private String activityType;
    //Post, Comment..
    private String objectType;
    private String objectId;
    //common
    private String text;
    private String imageKey;
    private String senderName;
    private String senderImageKey;

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getUnread() {
        return unread;
    }

    public void setUnread(Boolean unread) {
        this.unread = unread;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImageKey() {
        return senderImageKey;
    }

    public void setSenderImageKey(String senderImageKey) {
        this.senderImageKey = senderImageKey;
    }

    public String getSenderImageUrl(){
        return FileContentUrlUtils.getContentUrl(senderImageKey);
    }

    public String getImageUrl(){
        return FileContentUrlUtils.getContentUrl(imageKey);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "recipientId='" + recipientId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", activityType='" + activityType + '\'' +
                ", objectType='" + objectType + '\'' +
                ", objectId='" + objectId + '\'' +
                ", createdAt=" + createdAt +
                ", unread=" + unread +
                "} " + super.toString();
    }
}
