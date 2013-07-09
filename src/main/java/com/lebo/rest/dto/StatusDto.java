package com.lebo.rest.dto;

import com.lebo.entity.GeoLocation;
import com.lebo.entity.User;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

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
    private boolean isTruncated;

    //引用文件id，可以存视频、图片、或其他上传的文件
    private List<String> files;

    //该Tweet来源，如：手机客户端、网页版
    private String source;
    private GeoLocation geoLocation;

    private int favoriteCount;
    private int retweetCount;

    private boolean isRetweeted;
    private String originTweetId;

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

    public boolean isTruncated() {
        return isTruncated;
    }

    public void setTruncated(boolean truncated) {
        isTruncated = truncated;
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

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public void setRetweeted(boolean retweeted) {
        isRetweeted = retweeted;
    }

    public String getOriginTweetId() {
        return originTweetId;
    }

    public void setOriginTweetId(String originTweetId) {
        this.originTweetId = originTweetId;
    }
}
