package com.lebo.rest.dto;

import java.util.Date;

/**
 * <h2>User</h2>
 * <p/>
 * 字段描述见Twitter: https://dev.twitter.com/docs/platform-objects/users
 *
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: PM5:24
 */
public class UserDto {
    private String id;
    private String screenName;
    private String description;
    private String profileImageUrl;
    private Date createdAt;
    private Boolean following;
    private Integer followersCount;
    private Integer friendsCount;
    private Integer statusesCount;
    private Integer favoritesCount;
    private Integer beFavoritedCount;
    private Integer viewCount;
    private Integer digestCount;
    private Integer hotBeFavoritedCount; //仅热门用户列表有此字段
    private Boolean weiboVerified;
    //当前登录用户是否已将该用户加入黑名单
    private Boolean blocking;
    private Boolean bilateral;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public Integer getStatusesCount() {
        return statusesCount;
    }

    public void setStatusesCount(Integer statusesCount) {
        this.statusesCount = statusesCount;
    }

    public Integer getBeFavoritedCount() {
        return beFavoritedCount;
    }

    public void setBeFavoritedCount(Integer beFavoritedCount) {
        this.beFavoritedCount = beFavoritedCount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getWeiboVerified() {
        return weiboVerified;
    }

    public void setWeiboVerified(Boolean weiboVerified) {
        this.weiboVerified = weiboVerified;
    }

    public Boolean getBlocking() {
        return blocking;
    }

    public void setBlocking(Boolean blocking) {
        this.blocking = blocking;
    }

    public Boolean getBilateral() {
        return bilateral;
    }

    public void setBilateral(Boolean bilateral) {
        this.bilateral = bilateral;
    }

    public Integer getDigestCount() {
        return digestCount;
    }

    public void setDigestCount(Integer digestCount) {
        this.digestCount = digestCount;
    }

    public Integer getHotBeFavoritedCount() {
        return hotBeFavoritedCount;
    }

    public void setHotBeFavoritedCount(Integer hotBeFavoritedCount) {
        this.hotBeFavoritedCount = hotBeFavoritedCount;
    }
}
