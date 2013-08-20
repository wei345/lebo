package com.lebo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lebo.service.FileContentUrlUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * <h2>User</h2>
 * <p/>
 * <p>
 * 字段描述见Twitter: https://dev.twitter.com/docs/platform-objects/users
 * </p>
 * <p>
 * 新浪：http://open.weibo.com/wiki/%E5%B8%B8%E8%A7%81%E8%BF%94%E5%9B%9E%E5%AF%B9%E8%B1%A1%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84#.E7.94.A8.E6.88.B7.EF.BC.88user.EF.BC.89
 * </p>
 *
 * @author Wei Liu
 */
@Document
public class User extends IdEntity {
    @Indexed(unique = true)
    private String screenName;
    public static final String SCREEN_NAME_KEY = "screenName";
    @Indexed
    private String name;
    // The user-defined describing their account.
    private String description;
    //normal
    private String profileImageNormal;
    public static final String PROFILE_IMAGE_NORMAL_KEY = "profileImageNormal";
    public static final int PROFILE_IMAGE_NORMAL_SIZE = 50;
    //bigger
    private String profileImageBigger;
    public static final String PROFILE_IMAGE_BIGGER_KEY = "profileImageBigger";
    public static final int PROFILE_IMAGE_BIGGER_SIZE = 100;
    //origin 可以很大
    private String profileImageOriginal;
    public static final String PROFILE_IMAGE_ORIGIN_KEY = "profileImageOriginal";
    @Indexed
    private Date createdAt;
    private Boolean verified;
    //是否是新浪微博认证用户，即加V用户
    private Boolean weiboVerified;
    private String location;
    private String timeZone;

    // --- 下面是用户隐私数据 --- //
    @Indexed
    private LinkedHashSet<String> oAuthIds;
    // 性别 -1 未知，0 女，1 男
    private Integer gender;
    //TODO 在设置email时，保证唯一
    @Indexed
    private String email;
    private Date lastSignInAt;
    public static final String USER_LAST_SIGN_IN_AT_KEY = "lastSignInAt";

    // --- 本地用户 --- //
    @Transient
    private String plainPassword;
    private String password;
    private String salt;
    @Indexed
    private List<String> roles = new ArrayList<String>(1);

    //用户的粉丝数
    @Indexed(direction = IndexDirection.DESCENDING)
    private Integer followersCount;
    public static final String FOLLOWERS_COUNT_KEY = "followersCount";

    //用户Posts被收藏的次数
    @Indexed(direction = IndexDirection.DESCENDING)
    private Integer beFavoritedCount;
    public static final String BE_FAVORITED_COUNT_KEY = "beFavoritedCount";

    //用户Posts被播放的次数
    @Indexed(direction = IndexDirection.DESCENDING)
    private Integer viewCount;
    public static final String VIEW_COUNT_KEY = "viewCount";

    //用户精品视频数
    @Indexed(direction = IndexDirection.DESCENDING)
    private Integer digestCount;
    public static final String DIGEST_COUNT_KEY = "digestCount";

    //Apple Push Notification Service Token
    private String apnsProductionToken;
    private String apnsDevelopmentToken;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public User initial() {
        setFollowersCount(0);
        setViewCount(0);
        setBeFavoritedCount(0);
        return this;
    }

    public String getProfileImageNormal() {
        return profileImageNormal;
    }

    public void setProfileImageNormal(String profileImageNormal) {
        this.profileImageNormal = profileImageNormal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank
    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    // 设定JSON序列化时的日期格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public LinkedHashSet<String> getoAuthIds() {
        return oAuthIds;
    }

    public void setoAuthIds(LinkedHashSet<String> oAuthIds) {
        this.oAuthIds = oAuthIds;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastSignInAt() {
        return lastSignInAt;
    }

    public void setLastSignInAt(Date lastSignInAt) {
        this.lastSignInAt = lastSignInAt;
    }


    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
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

    public Boolean getWeiboVerified() {
        return weiboVerified;
    }

    public void setWeiboVerified(Boolean weiboVerified) {
        this.weiboVerified = weiboVerified;
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

    public Integer getDigestCount() {
        return digestCount;
    }

    public void setDigestCount(Integer digestCount) {
        this.digestCount = digestCount;
    }

    public String getProfileImageUrl() {
        return FileContentUrlUtils.getContentUrl(profileImageNormal);
    }

    public String getProfileImage() {
        return profileImageNormal;
    }

    public String getProfileImageBigger() {
        return profileImageBigger;
    }

    public String getProfileImageBiggerUrl() {
        return FileContentUrlUtils.getContentUrl(profileImageBigger);
    }

    public void setProfileImageBigger(String profileImageBigger) {
        this.profileImageBigger = profileImageBigger;
    }

    public String getProfileImageOriginal() {
        return profileImageOriginal;
    }

    public String getProfileImageOriginalUrl() {
        return FileContentUrlUtils.getContentUrl(profileImageOriginal);
    }

    public void setProfileImageOriginal(String profileImageOriginal) {
        this.profileImageOriginal = profileImageOriginal;
    }

    @Override
    public String toString() {
        return "User{" +
                "screenName='" + screenName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", profileImageNormal='" + profileImageNormal + '\'' +
                ", createdAt=" + createdAt +
                ", verified=" + verified +
                ", weiboVerified=" + weiboVerified +
                ", location='" + location + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", oAuthIds=" + oAuthIds +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", lastSignInAt=" + lastSignInAt +
                ", plainPassword='" + plainPassword + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", roles=" + roles +
                ", followersCount=" + followersCount +
                ", beFavoritedCount=" + beFavoritedCount +
                ", viewCount=" + viewCount +
                ", digestCount=" + digestCount +
                ", apnsProductionToken='" + apnsProductionToken + '\'' +
                ", apnsDevelopmentToken='" + apnsDevelopmentToken + '\'' +
                "} " + super.toString();
    }
}