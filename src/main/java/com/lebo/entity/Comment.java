package com.lebo.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-4
 * Time: PM4:34
 */
@Document
public class Comment extends IdEntity {
    public static final String POST_ID_KEY = "postId";
    @Indexed
    private String userId;
    private Date createdAt;
    private String text;
    private boolean truncated;
    //引用文件id，可以存视频、图片、或其他上传的文件
    private List<FileInfo> files;
    //来源，如：手机客户端、网页版
    private String source;
    private GeoLocation geoLocation;
    // 被评论的post
    private String postId;
    //被回复的评论id，可能为null，不会被级联删除
    private String replyCommentId;
    //被回复的评论的作者id，冗余，为更好的性能
    private String replyCommentUserId;
    // 如果被评论post是转发的，则该评论也要归结到原始post上
    private String originId;
    @Indexed
    private LinkedHashSet<String> mentions;
    @Indexed
    private Boolean hasVideo;
    public static final String HAS_VIDEO_KEY = "hasVideo";

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

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public LinkedHashSet<String> getMentions() {
        return mentions;
    }

    public void setMentions(LinkedHashSet<String> mentions) {
        this.mentions = mentions;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
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

    public String getReplyCommentUserId() {
        return replyCommentUserId;
    }

    public void setReplyCommentUserId(String replyCommentUserId) {
        this.replyCommentUserId = replyCommentUserId;
    }

    public List<FileInfo> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }
}
