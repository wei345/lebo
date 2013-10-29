package com.lebo.rest.dto;

import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM2:26
 */
public class ImDto {
    private UserDto from;
    private UserDto to;
    private List<FileInfoDto> attachments;
    private Date createdAt;

    public UserDto getFrom() {
        return from;
    }

    public void setFrom(UserDto from) {
        this.from = from;
    }

    public UserDto getTo() {
        return to;
    }

    public void setTo(UserDto to) {
        this.to = to;
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
}
