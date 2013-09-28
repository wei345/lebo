package com.lebo.entity;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 由管理员的定时执行的任务。
 */
@Document(collection = "admin.tasks")
public class Task extends IdEntity {
    public static final String STATUS_VALUE_DONE = "done";
    public static final String STATUS_VALUE_TODO = "todo";
    public static final String TYPE_VALUE_PUBLISH_VIDEO = "publish-video";

    private String title;
    private String description;
    private String userId;//该任务创建者
    private String type;
    public static final String TYPE_KEY = "type";
    private Date scheduledAt;
    public static final String SCHEDULED_AT_KEY = "scheduledAt";
    private Date createdAt;
    private String status;
    public static final String STATUS_KEY = "status";
    //-- 定时发布视频 --
    private FileInfo video;
    private FileInfo videoFirstFrame;
    private String videoText;
    private String videoUserId;
    private String videoSource;

    // JSR303 BeanValidator的校验规则
    @NotBlank
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(Date scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FileInfo getVideo() {
        return video;
    }

    public void setVideo(FileInfo video) {
        this.video = video;
    }

    public FileInfo getVideoFirstFrame() {
        return videoFirstFrame;
    }

    public void setVideoFirstFrame(FileInfo videoFirstFrame) {
        this.videoFirstFrame = videoFirstFrame;
    }

    public String getVideoText() {
        return videoText;
    }

    public void setVideoText(String videoText) {
        this.videoText = videoText;
    }

    public String getVideoUserId() {
        return videoUserId;
    }

    public void setVideoUserId(String videoUserId) {
        this.videoUserId = videoUserId;
    }

    public String getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(String videoSource) {
        this.videoSource = videoSource;
    }
}