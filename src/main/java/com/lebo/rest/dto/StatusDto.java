package com.lebo.rest.dto;

import java.util.ArrayList;
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
    //引用文件id，可以存视频、图片、或其他上传的文件
    private List<FileInfoDto> files = new ArrayList<FileInfoDto>(2);
    //该Tweet来源，如：手机客户端、网页版
    private String source;
    private Boolean favorited;
    private Integer favoritesCount;
    private Integer repostsCount;
    private Boolean reposted;
    private Integer commentsCount;
    private Integer viewsCount;
    private StatusDto originStatus;
    //最近3条评论
    private List<CommentDto> comments;
    private List<UserDto> userMentions;
    private Boolean digested;//是否已被加精

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

    public List<FileInfoDto> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfoDto> files) {
        this.files = files;
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

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public List<UserDto> getUserMentions() {
        return userMentions;
    }

    public void setUserMentions(List<UserDto> userMentions) {
        this.userMentions = userMentions;
    }

    public Boolean getDigested() {
        return digested;
    }

    public void setDigested(Boolean digested) {
        this.digested = digested;
    }

    public static class FileInfoDto {
        protected String filename;
        protected int length;
        protected String contentType;
        protected String contentUrl;

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }
    }
}
