package com.lebo.rest.dto;

import com.lebo.entity.GeoLocation;
import com.lebo.entity.User;

import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: PM1:35
 */
public class StatusDto {
    private String id;
    private User user;
    private Date createdAt;
    private String text;
    private Boolean truncated;
    //引用文件id，可以存视频、图片、或其他上传的文件
    private List<String> files;
    //该Tweet来源，如：手机客户端、网页版
    private String source;
    private GeoLocation geoLocation;
    private Integer favouritesCount;
    private Integer repostsCount;
    private Boolean reposted;
    private String originPostId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
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

    public Boolean getTruncated() {
        return truncated;
    }

    public void setTruncated(Boolean truncated) {
        this.truncated = truncated;
    }

    public Integer getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(Integer favouritesCount) {
        this.favouritesCount = favouritesCount;
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

    public String getOriginPostId() {
        return originPostId;
    }

    public void setOriginPostId(String originPostId) {
        this.originPostId = originPostId;
    }
}
