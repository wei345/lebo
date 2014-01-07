package com.lebo.entity;

import com.lebo.service.FileContentUrlUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 由管理员的定时执行的任务。
 */
@Document(collection = "admin.tasks")
public class Task extends IdEntity {

    private String title;
    private String description;
    private String userId;//任务创建者
    private Type type;
    public static final String TYPE_KEY = "type";
    private Date scheduledAt;
    public static final String SCHEDULED_AT_KEY = "scheduledAt";
    private Date createdAt;
    private Status status;
    public static final String STATUS_KEY = "status";
    private String taskData; //任务数据json
    //-- 定时发布视频 --//
    private FileInfo video;
    private FileInfo videoFirstFrame;
    private String videoText;
    private String videoUserId;
    private String videoSource;
    //-- 发布通知 --//
    private String notificationText;
    private String notificationImageKey;
    private String notificationSenderName;
    private String notificationSenderImageKey;
    private Integer notificationSentCount;
    private Integer notificationApnsCount;

    public Task() {
    }

    public Task(Type type, String userId, Date createdAt, String title) {
        this.type = type;
        this.userId = userId;
        this.createdAt = createdAt;
        this.title = title;
    }

    public static enum Type {
        PUBLISH_VIDEO,
        APNS_ALL_USER,
        ROBOT_COMMENT
    }

    public static enum Status {
        TODO, DONE
    }

    public static class RobotComment {
        private String postId;
        private String userId;
        private String text;

        public RobotComment() {
        }

        public RobotComment(String postId, String userId, String text) {
            this.postId = postId;
            this.userId = userId;
            this.text = text;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public Integer getNotificationSentCount() {
        return notificationSentCount;
    }

    public void setNotificationSentCount(Integer notificationSentCount) {
        this.notificationSentCount = notificationSentCount;
    }

    public String getNotificationImageKey() {
        return notificationImageKey;
    }

    public String getNotificationImageUrl() {
        return FileContentUrlUtils.getContentUrl(notificationImageKey);
    }

    public void setNotificationImageKey(String notificationImageKey) {
        this.notificationImageKey = notificationImageKey;
    }

    public Integer getNotificationApnsCount() {
        return notificationApnsCount;
    }

    public void setNotificationApnsCount(Integer notificationApnsCount) {
        this.notificationApnsCount = notificationApnsCount;
    }

    public String getNotificationSenderName() {
        return notificationSenderName;
    }

    public void setNotificationSenderName(String notificationSenderName) {
        this.notificationSenderName = notificationSenderName;
    }

    public String getNotificationSenderImageKey() {
        return notificationSenderImageKey;
    }

    public void setNotificationSenderImageKey(String notificationSenderImageKey) {
        this.notificationSenderImageKey = notificationSenderImageKey;
    }

    public String getNotificationSenderImageUrl() {
        return FileContentUrlUtils.getContentUrl(notificationSenderImageKey);
    }

    public String getTaskData() {
        return taskData;
    }

    public void setTaskData(String taskData) {
        this.taskData = taskData;
    }
}