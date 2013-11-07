package com.lebo.entity;

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
    private int type;
    private Date createdAt;
    public static final String CREATED_AT = "createdAt";


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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
