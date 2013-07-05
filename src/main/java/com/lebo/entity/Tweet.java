package com.lebo.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-7-3
 * Time: PM12:22
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "tweet_unique", def = "{'user': 1, 'text': 1, 'media': 1}", unique = true, dropDups = true)
})
public class Tweet extends IdEntity {
    private Date createdAt;
    private String text;
    @DBRef
    private FsFiles media;
    private String source;
    private boolean isTruncated;
    private boolean isRetweeted;
    private int retweetCount;
    private int favoriteCount;
    @DBRef
    private User user;
    private GeoLocation geoLocation;

    private boolean isRetweet;
    @DBRef
    private Tweet originTweet;

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

    public FsFiles getMedia() {
        return media;
    }

    public void setMedia(FsFiles media) {
        this.media = media;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public void setTruncated(boolean truncated) {
        isTruncated = truncated;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public void setRetweeted(boolean retweeted) {
        isRetweeted = retweeted;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public boolean isRetweet() {
        return isRetweet;
    }

    public void setRetweet(boolean retweet) {
        isRetweet = retweet;
    }

    public Tweet getOriginTweet() {
        return originTweet;
    }

    public void setOriginTweet(Tweet originTweet) {
        this.originTweet = originTweet;
    }
}
