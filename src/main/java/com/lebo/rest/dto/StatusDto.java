package com.lebo.rest.dto;

import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: PM1:35
 */
public class StatusDto {
    private String id;
    private UserDto user;
    private Date createdAt;
    private String text;
    private FileInfoDto video;
    private FileInfoDto videoConverted;
    private String videoFirstFrameUrl;
    //该Tweet来源，如：手机客户端、网页版
    private String source;
    private Boolean favorited;
    private Integer favoritesCount;
    private Integer repostsCount;
    private Boolean reposted;
    private Integer commentsCount;
    private Integer viewCount;
    private Integer hotFavoritesCount; //仅热门帖子列表有此字段
    private StatusDto originStatus;
    //最近3条评论
    private List<CommentDto> comments;
    private List<UserMentionDto> userMentions;
    private Boolean digest;//是否已被加精

    public static class UserMentionDto {
        private String userId;
        private String screenName;
        private List<Integer> indices;

        public String getId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getScreenName() {
            return screenName;
        }

        public void setScreenName(String screenName) {
            this.screenName = screenName;
        }

        public List<Integer> getIndices() {
            return indices;
        }

        public void setIndices(List<Integer> indices) {
            this.indices = indices;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(Integer favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public Integer getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(Integer repostsCount) {
        this.repostsCount = repostsCount;
    }

    public Boolean getReposted() {
        return reposted;
    }

    public void setReposted(Boolean reposted) {
        this.reposted = reposted;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public StatusDto getOriginStatus() {
        return originStatus;
    }

    public void setOriginStatus(StatusDto originStatus) {
        this.originStatus = originStatus;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public List<UserMentionDto> getUserMentions() {
        return userMentions;
    }

    public void setUserMentions(List<UserMentionDto> userMentions) {
        this.userMentions = userMentions;
    }

    public Boolean getDigest() {
        return digest;
    }

    public void setDigest(Boolean digest) {
        this.digest = digest;
    }

    public Integer getHotFavoritesCount() {
        return hotFavoritesCount;
    }

    public void setHotFavoritesCount(Integer hotFavoritesCount) {
        this.hotFavoritesCount = hotFavoritesCount;
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

    public FileInfoDto getVideoConverted() {
        return videoConverted;
    }

    public void setVideoConverted(FileInfoDto videoConverted) {
        this.videoConverted = videoConverted;
    }
}
