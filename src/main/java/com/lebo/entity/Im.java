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
public class Im extends IdEntity{
    private String from;
    private String to;
    private String message;
    private List<FileInfo> attachments;
    private Date createdAt;
    public static final String CREATED_AT = "createdAt";

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

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
}
