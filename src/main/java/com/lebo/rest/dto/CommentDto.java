package com.lebo.rest.dto;

import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-7-25
 * Time: PM1:43
 */
public class CommentDto {
    private String id;
    private String postId;
    private Date createdAt;
    private String text;
    private FileInfoDto video;
    private FileInfoDto videoConverted;
    private String videoFirstFrameUrl;
    private FileInfoDto audio;
    private Boolean hasVideo;
    private UserDto user;
    private String replyCommentId;
    private UserDto replyCommentUser;

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

    public String getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(String replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public UserDto getReplyCommentUser() {
        return replyCommentUser;
    }

    public void setReplyCommentUser(UserDto replyCommentUser) {
        this.replyCommentUser = replyCommentUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public FileInfoDto getVideo() {
        return video;
    }

    public void setVideo(FileInfoDto video) {
        this.video = video;
    }

    public String getVideoFirstFrameUrl() {
        return videoFirstFrameUrl;
    }

    public void setVideoFirstFrameUrl(String videoFirstFrameUrl) {
        this.videoFirstFrameUrl = videoFirstFrameUrl;
    }

    public FileInfoDto getAudio() {
        return audio;
    }

    public void setAudio(FileInfoDto audio) {
        this.audio = audio;
    }

    public FileInfoDto getVideoConverted() {
        return videoConverted;
    }

    public void setVideoConverted(FileInfoDto videoConverted) {
        this.videoConverted = videoConverted;
    }
}
