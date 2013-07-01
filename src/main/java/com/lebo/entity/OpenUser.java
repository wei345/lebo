package com.lebo.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author: Wei Liu
 * Date: 13-7-1
 * Time: AM11:46
 */
@Document
@CompoundIndexes({
    @CompoundIndex(name = "platform_uid", def = "{'platform': 1, 'uid': 1}", unique = true)
})
public class OpenUser extends IdEntity{

    private String provider; // 开放平台，例如weibo表示新浪微博
    private String uid;     // 用户在特定开放平台中的id
    private String screenName; // 用户在特定开放平台中的screen name
    private String name;
    private String profileUrl;
    private String profileImageUrl;
    private String localUserId; // 乐播用户id

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getLocalUserId() {
        return localUserId;
    }

    public void setLocalUserId(String localUserId) {
        this.localUserId = localUserId;
    }
}
