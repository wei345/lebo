package com.lebo.entity;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * 即时通讯.
 *
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: AM11:40
 */
@Document(collection = "ims")
public class Im extends IdEntity {
    private String fromUserId;
    private String toUserId;
    public static final String TO_USER_ID = "toUserId";
    private String message;
    private List<FileInfo> attachments;
    private Integer type;
    public static final String TYPE_KEY = "type";
    private Date createdAt;
    @Indexed(direction = IndexDirection.DESCENDING)
    private Long messageTime;  //客户端提交的消息时间
    public static final String MESSAGE_TIME_KEY = "messageTime";
    private Boolean unread;
    public static final String UNREAD_KEY = "unread";

    public List<FileInfo> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<FileInfo> attachments) {
        this.attachments = attachments;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }

    public Boolean getUnread() {
        return unread;
    }

    public void setUnread(Boolean unread) {
        this.unread = unread;
    }
}
