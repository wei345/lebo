package com.lebo.rest.dto;

import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-8-8
 * Time: PM7:20
 */
public class NotificationDto {
    private String id;
    //触发通知的用户
    private UserDto sender;
    private String activityType;
    //当activityType为收藏、转发、视频at、评论at、回复视频、回复评论时，relatedStatus有值，如果为null(返回值没有该字段)则表示该视频已被删除。
    private StatusDto relatedStatus;
    //当activityType为评论at、回复视频、回复评论时，relatedComment有值，如果为null(返回值没有该字段)则表示该评论已被删除。
    private CommentDto relatedComment;
    private Date createdAt;
    private Boolean unread;

    public UserDto getSender() {
        return sender;
    }

    public void setSender(UserDto sender) {
        this.sender = sender;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
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

    public StatusDto getRelatedStatus() {
        return relatedStatus;
    }

    public void setRelatedStatus(StatusDto relatedStatus) {
        this.relatedStatus = relatedStatus;
    }

    public CommentDto getRelatedComment() {
        return relatedComment;
    }

    public void setRelatedComment(CommentDto relatedComment) {
        this.relatedComment = relatedComment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
