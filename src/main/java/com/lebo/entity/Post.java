package com.lebo.entity;

import com.lebo.rest.dto.StatusDto;
import org.springframework.data.mongodb.core.index.*;
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
@Document(collection = "posts")
@CompoundIndexes({
        @CompoundIndex(name = "uid_1_opuid_1", def = "{'userId': 1, 'originPostUserId': 1}"),
        @CompoundIndex(name = "uid_1_opid_1", def = "{'userId': 1, 'originPostId': 1}")
})
//TODO 增加是否转发字段，如果原始帖子被删除，则originPostId为null?
public class Post extends IdEntity {
    private String userId;
    public static final String USER_ID_KEY = "userId";
    @Indexed(direction = IndexDirection.DESCENDING)
    private Date createdAt;
    public static final String CREATED_AT_KEY = "createdAt";

    private String text;
    private boolean truncated;
    //视频(mp4)
    private FileInfo video;
    public static final String VIDEO_KEY = "video";
    private FileInfo videoConverted;
    public static final String VIDEO_CONVERTED_KEY = "videoConverted";
    private String videoConvertStatus;
    public static final String VIDEO_CONVERT_STATUS_KEY = "videoConvertStatus";

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
    private LinkedHashSet<String> mentionUserIds;
    public static final String MENTION_USER_IDS = "mentionUserIds";
    private List<UserMention> userMentions;
    @Indexed
    private LinkedHashSet<String> hashtags;
    public static final String HASHTAGS_KEY = "hashtags";
    //存储text分词结果、标签、URL，专用于搜索
    @Indexed
    private LinkedHashSet<String> searchTerms;
    public static final String SEARCH_TERMS_KEY = "searchTerms";

    @Indexed(direction = IndexDirection.DESCENDING)
    private Integer favoritesCount;
    public static final String FAVOURITES_COUNT_KEY = "favoritesCount";

    private Integer repostsCount;
    public static final String REPOSTS_COUNT_KEY = "repostsCount";

    private Integer commentsCount;
    public static final String COMMENTS_COUNT_KEY = "commentsCount";

    private Integer viewCount;
    public static final String VIEW_COUNT_KEY = "viewCount";

    private Integer shareCount;
    public static final String SHARE_COUNT_KEY = "shareCount";

    @Indexed(direction = IndexDirection.DESCENDING)
    private Date lastCommentCreatedAt;
    public static final String LAST_COMMENT_CREATED_AT_KEY = "lastCommentCreatedAt";

    // 是否精品
    private boolean digest;
    public static final String DIGEST_KEY = "digest";

    private Integer rating;
    public static final String RATING_KEY = "rating";

    //TODO 临时添加pikeId为了能够正常登录，待上线新服务端稳定后去掉
    private String pikeId;

    /**
     * 访问控制列表
     * <ul>
     * <li>1 相互关注的人可见</li>
     * <li>2 粉丝(不含相互关注)可见</li>
     * <li>4 其他人可见</li>
     * <li><code>null</code>和<code>7</code>(1 + 2 + 4)一样表示所有人可以访问</li>
     * <li>所有者总是可见</li>
     * <li>转发帖不受权限限制</li>
     * </ul>
     * <p/>
     * <p>
     * 该字段只在服务端内部使用，对客户端不可见。服务端可以随时重新定义此字段。
     * </p>
     * <p/>
     * <p>
     * 目前使用的值: null, 0
     * </p>
     */
    @Indexed
    private Integer acl;
    public static final String ACL_KEY = "acl";
    /**
     * 只有所有者可见
     */
    public static final Integer ACL_PRIVATE = 0;
    public static final Integer ACL_PUBLIC = null;

    private Integer popularity; //人气
    public static final String POPULARITY_KEY = "popularity";

    @Indexed(direction = IndexDirection.DESCENDING)
    private Integer favoritesCountAddPopularity; //favoritesCount + popularity
    public static final String FAVORITES_COUNT_ADD_POPULARITY_KEY = "favoritesCountAddPopularity";

    public static class UserMention {
        private String userId;
        private String screenName;
        private List<Integer> indices;

        public String getUserId() {
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

        public StatusDto.UserMentionDto toDto() {
            StatusDto.UserMentionDto dto = new StatusDto.UserMentionDto();
            dto.setUserId(getUserId());
            dto.setScreenName(getScreenName());
            dto.setIndices(getIndices());
            return dto;
        }

        public static List<StatusDto.UserMentionDto> toDtos(List<UserMention> userMentions) {
            List<StatusDto.UserMentionDto> dtos = new ArrayList<StatusDto.UserMentionDto>(userMentions.size());
            for (UserMention userMention : userMentions) {
                dtos.add(userMention.toDto());
            }
            return dtos;
        }
    }

    public Post initialCounts() {
        viewCount = 0;
        favoritesCount = 0;
        repostsCount = 0;
        commentsCount = 0;
        shareCount = 0;
        popularity = 0;
        favoritesCountAddPopularity = 0;
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

    public LinkedHashSet<String> getMentionUserIds() {
        return mentionUserIds;
    }

    public void setMentionUserIds(LinkedHashSet<String> mentionUserIds) {
        this.mentionUserIds = mentionUserIds;
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

    public String getVideoFirstFrameUrl() {
        if (videoFirstFrame == null) {
            return null;
        }
        return videoFirstFrame.getContentUrl();
    }

    public List<UserMention> getUserMentions() {
        return userMentions;
    }

    public void setUserMentions(List<UserMention> userMentions) {
        this.userMentions = userMentions;
    }

    public String getPikeId() {
        return pikeId;
    }

    public void setPikeId(String pikeId) {
        this.pikeId = pikeId;
    }

    public String getVideoConvertStatus() {
        return videoConvertStatus;
    }

    public void setVideoConvertStatus(String videoConvertStatus) {
        this.videoConvertStatus = videoConvertStatus;
    }

    public FileInfo getVideoConverted() {
        return videoConverted;
    }

    public void setVideoConverted(FileInfo videoConverted) {
        this.videoConverted = videoConverted;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getAcl() {
        return acl;
    }

    public void setAcl(Integer acl) {
        this.acl = acl;
    }

    public boolean getPvt() {
        return (acl != null && acl == ACL_PRIVATE);
    }

    public boolean isPublic() {
        return acl == null;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(Integer repostsCount) {
        this.repostsCount = repostsCount;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public boolean isRepost() {
        return originPostId != null;
    }

    public boolean isOriginPost() {
        return originPostId == null;
    }

    public Date getLastCommentCreatedAt() {
        return lastCommentCreatedAt;
    }

    public void setLastCommentCreatedAt(Date lastCommentCreatedAt) {
        this.lastCommentCreatedAt = lastCommentCreatedAt;
    }

    public Integer getFavoritesCountAddPopularity() {
        return favoritesCountAddPopularity;
    }

    public void setFavoritesCountAddPopularity(Integer favoritesCountAddPopularity) {
        this.favoritesCountAddPopularity = favoritesCountAddPopularity;
    }
}
