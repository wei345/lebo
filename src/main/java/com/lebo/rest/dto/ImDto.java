package com.lebo.rest.dto;

import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM2:26
 */
public class ImDto {
    private String id;
    private String fromUserId;
    private String toUserId;
    private String message;
    private Integer type;
    private List<FileInfoDto> attachments;
    private Date createdAt;
    private Long messageTime;

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<FileInfoDto> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<FileInfoDto> attachments) {
        this.attachments = attachments;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }
}
