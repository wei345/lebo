package com.lebo.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-3
 * Time: PM12:22
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "tweet_unique", def = "{'user': 1, 'text': 1, 'files': 1}", unique = true)
})
public class Tweet extends IdEntity {
    @Indexed
    private String userId;
    @Indexed(direction = IndexDirection.DESCENDING)
    private Date createdAt;
    private String text;
    private boolean truncated;

    //引用文件id，可以存视频、图片、或其他上传的文件
    private List<String> files;

    //该Tweet来源，如：手机客户端、网页版
    private String source;
    private GeoLocation geoLocation;

    @Indexed
    private int favoriteCount;
    @Indexed
    private int retweetCount;

    private boolean retweeted;
    @Indexed
    private String originTweetId;

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

    public String getOriginTweetId() {
        return originTweetId;
    }

    public void setOriginTweetId(String originTweetId) {
        this.originTweetId = originTweetId;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }
    //TODO 检查数据库字段，应该是retweeted，而不是isRetweeted
    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }
}
