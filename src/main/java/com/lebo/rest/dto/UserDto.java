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
    private String name;
    private String profileImageUrl;
    //TODO 日期转为UTC格式，如：Mon Nov 29 21:18:15 +0000 2010
    private Date createdAt;
    private Boolean following;
    //TODO 使用缓存处理followersCount，读通过，写通过
    private Integer followersCount;
    //TODO 不输出null字段
    private Integer friendsCount;
    //TODO 填充全部字段
    private Integer statusesCount;
    private Integer beFavoritedCount;
    private Integer viewsCount;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }
}
