package com.lebo.rest.dto;

/**
 * 用户的隐私信息。
 *
 * @author: Wei Liu
 * Date: 13-9-12
 * Time: PM1:38
 */
public class AccountSettingDto {
    private String id;
    private String screenName;
    private String description = "";
    private String profileImageUrl;
    private String profileImageBiggerUrl;
    private String profileImageOriginalUrl;
    private Boolean notifyOnReplyPost = true;
    private Boolean notifyOnFavorite = true;
    private Boolean notifyOnFollow = true;
    private Boolean notifySound = false;
    private Boolean notifyVibrator = false;
    private String apnsProductionToken = "";
    private String apnsDevelopmentToken = "";

    public Boolean getNotifyOnReplyPost() {
        return notifyOnReplyPost;
    }

    public void setNotifyOnReplyPost(Boolean notifyOnReplyPost) {
        this.notifyOnReplyPost = notifyOnReplyPost;
    }

    public Boolean getNotifyOnFavorite() {
        return notifyOnFavorite;
    }

    public void setNotifyOnFavorite(Boolean notifyOnFavorite) {
        this.notifyOnFavorite = notifyOnFavorite;
    }

    public Boolean getNotifyOnFollow() {
        return notifyOnFollow;
    }

    public void setNotifyOnFollow(Boolean notifyOnFollow) {
        this.notifyOnFollow = notifyOnFollow;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageBiggerUrl() {
        return profileImageBiggerUrl;
    }

    public void setProfileImageBiggerUrl(String profileImageBiggerUrl) {
        this.profileImageBiggerUrl = profileImageBiggerUrl;
    }

    public String getProfileImageOriginalUrl() {
        return profileImageOriginalUrl;
    }

    public void setProfileImageOriginalUrl(String profileImageOriginalUrl) {
        this.profileImageOriginalUrl = profileImageOriginalUrl;
    }

    public String getApnsProductionToken() {
        return apnsProductionToken;
    }

    public void setApnsProductionToken(String apnsProductionToken) {
        this.apnsProductionToken = apnsProductionToken;
    }

    public String getApnsDevelopmentToken() {
        return apnsDevelopmentToken;
    }

    public void setApnsDevelopmentToken(String apnsDevelopmentToken) {
        this.apnsDevelopmentToken = apnsDevelopmentToken;
    }

    public Boolean getNotifySound() {
        return notifySound;
    }

    public void setNotifySound(Boolean notifySound) {
        this.notifySound = notifySound;
    }

    public Boolean getNotifyVibrator() {
        return notifyVibrator;
    }

    public void setNotifyVibrator(Boolean notifyVibrator) {
        this.notifyVibrator = notifyVibrator;
    }
}
