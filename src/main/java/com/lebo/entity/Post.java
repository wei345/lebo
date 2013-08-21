package com.lebo.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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
        @CompoundIndex(name = "post_unique", def = "{'userId': 1, 'text': 1, 'files': 1, 'originPostId': 1}", unique = true),
        @CompoundIndex(name = "uid_1_opuid_1", def = "{'userId': 1, 'originPostUserId': 1}"),
        @CompoundIndex(name = "uid_1_opid_1", def = "{'userId': 1, 'originPostId': 1}")
})
//TODO 增加是否转发字段，如果原始帖子被删除，则originPostId为null?
public class Post extends IdEntity {
    private String userId;
    public static final String USER_ID_KEY = "userId";
    @Indexed
    private Date createdAt;
    public static final String CREATED_AT_KEY = "createdAt";

    private String text;
    private boolean truncated;
    //视频(mp4)
    private FileInfo video;
    //视频第一帧(图片)
    private FileInfo videoFirstFrame;
    //来源，如：手机客户端、网页版
    private String source;
    @GeoSpatialIndexed
    private GeoLocation geoLocation;
    @Indexed
    private String originPostId;
    public static final String ORIGIN_POST_ID_KEY = "originPostId";
    private String originPostUserId;
    public static final String ORIGIN_POST_USER_ID_KEY = "originPostUserId";
    //提到的用户的ID
    @Indexed
    private LinkedHashSet<String> userMentions;
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

    public String getOriginPostUserId() {
        return originPostUserId;
    }

    public void setOriginPostUserId(String originPostUserId) {
        this.originPostUserId = originPostUserId;
    }

    public List<FileInfo> getFiles(){
        List<FileInfo> fileInfos = new ArrayList<FileInfo>(2);
        if(video != null){
            fileInfos.add(video);
        }
        if(videoFirstFrame != null){
            fileInfos.add(videoFirstFrame);
        }
        return fileInfos;
    }

    public String getVideoFirstFrameUrl(){
        if(videoFirstFrame == null){
            return null;
        }
        return videoFirstFrame.getContentUrl();
    }
}
