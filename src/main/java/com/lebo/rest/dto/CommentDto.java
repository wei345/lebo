package com.lebo.rest.dto;

import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-25
 * Time: PM1:43
 */
public class CommentDto {
    private Date createdAt;
    private String text;
    private List<StatusDto.FileInfoDto> files;

    private Boolean hasVideo;
    private UserDto user;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<StatusDto.FileInfoDto> getFiles() {
        return files;
    }

    public void setFiles(List<StatusDto.FileInfoDto> files) {
        this.files = files;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Boolean getHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(Boolean hasVideo) {
        this.hasVideo = hasVideo;
    }
}
