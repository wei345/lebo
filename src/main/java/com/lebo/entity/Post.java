package com.lebo.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.LinkedHashSet;
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
public class Post extends IdEntity {
    @Indexed
    private String userId;
    public static final String USER_ID_KEY = "userId";
    @Indexed
    private Date createdAt;
    public static final String CREATED_AT_KEY = "createdAt";

    private String text;
    private boolean truncated;
    //引用文件id，可以存视频、图片、或其他上传的文件
    private List<String> files;
    //来源，如：手机客户端、网页版
    private String source;
    @GeoSpatialIndexed
    private GeoLocation geoLocation;
    @Indexed
    private String originPostId;
    public static final String ORIGIN_POST_ID_KEY = "originPostId";
    //提到的用户的ID
    @Indexed
    private LinkedHashSet<String> userMentions;
    //TODO 有terms了，删除tags?
    @Indexed
    private LinkedHashSet<String> hashtags;
    public static final String HASHTAGS_KEY = "hashtags";
    //存储text分词结果、标签、URL，专用于搜索
    @Indexed
    private LinkedHashSet<String> searchTerms;
    public static final String SEARCH_TERMS_KEY = "searchTerms";

    private Integer favoritesCount;
    public static final String FAVOURITES_COUNT_KEY = "favoritesCount";

    private Integer viewCount;
    public static final String VIEW_COUNT_KEY = "viewCount";

    // 是否精品
    private boolean digest;
    public static final String DIGEST_KEY = "digest";

    public Post initial() {
        setViewCount(0);
        setFavoritesCount(0);
        return this;
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

    //TODO 检查数据库字段，应该是truncated，isTruncated
    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getOriginPostId() {
        return originPostId;
    }

    public void setOriginPostId(String originPostId) {
        this.originPostId = originPostId;
    }

    public LinkedHashSet<String> getUserMentions() {
        return userMentions;
    }

    public void setUserMentions(LinkedHashSet<String> userMentions) {
        this.userMentions = userMentions;
    }

    public LinkedHashSet<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(LinkedHashSet<String> hashtags) {
        this.hashtags = hashtags;
    }

    public LinkedHashSet<String> getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(LinkedHashSet<String> searchTerms) {
        this.searchTerms = searchTerms;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(Integer favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public boolean getDigest() {
        return digest;
    }

    public void setDigest(boolean digest) {
        this.digest = digest;
    }
}
