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
@Document(collection = "users")
public class User extends IdEntity {
    @Indexed(unique = true)
    private String screenName;
    public static final String SCREEN_NAME_KEY = "screenName";
    @Indexed
    private String name;
    public static final String NAME_KEY = "name";
    // The user-defined describing their account.
    private String description;
    public static final String DESCRIPTION_KEY = "description";
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
    //background image
    private String profileBackgroundImage;
    public static final String PROFILE_BACKGROUND_IMAGE_KEY = "profileBackgroundImage";
    public static final int PROFILE_BACKGROUND_IMAGE_SIZE = 1024;
    @Indexed
    private Date createdAt;
    public static final String CREATED_AT_KEY = "createdAt";
    private Boolean verified;
    //是否是新浪微博认证用户，即加V用户
    private Boolean weiboVerified;
    public static final String WEIBO_VERIFIED_KEY = "weiboVerified";
    private String location;
    private String timeZone;

    // --- 下面是用户隐私数据 --- //
    @Indexed
    private LinkedHashSet<String> oAuthIds;
    private String gender;
    public static final String GENDER_KEY = "gender";
    public static final String GENDER_MALE = "m";
    public static final String GENDER_FEMALE = "f";
    //TODO 在设置email时，保证唯一
    @Indexed
    private String email;
    public static final String EMAIL_KEY = "email";
    private Date lastSignInAt;
    public static final String USER_LAST_SIGN_IN_AT_KEY = "lastSignInAt";

    // --- 本地用户 --- //
    @Transient
    private String plainPassword;
    private String password;
    public static final String PASSWORD_KEY = "password";
    private String salt;
    public static final String SALT_KEY = "salt";
    @Indexed
    private List<String> roles = new ArrayList<String>(1);
    public static final String ROLES_KEY = "roles";
    public static final String ROLES_ADMIN = "admin";

    //用户的粉丝数
    @Indexed(direction = IndexDirection.DESCENDING)
    private Integer followersCount;
    public static final String FOLLOWERS_COUNT_KEY = "followersCount";

    //用户的好友数(关注数)
    private Integer friendsCount;
    public static final String FRIENDS_COUNT_KEY = "friendsCount";

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

    //用户视频数
    private Integer statusesCount;
    public static final String STATUSES_COUNT_KEY = "statusesCount";
    private Integer originPostsCount;
    public static final String ORIGIN_POSTS_COUNT_KEY = "originPostsCount";
    private Integer repostsCount;
    public static final String REPOSTS_COUNT_KEY = "repostsCount";

    //Apple Push Notification Service Token
    private String apnsProductionToken = "";
    public static final String APNS_PRODUCTION_TOKEN_KEY = "apnsProductionToken";
    private String apnsDevelopmentToken = ""; //APNS Development Token由于p12文件问题，暂不可用。
    public static final String APNS_DEVELOPMENT_TOKEN_KEY = "apnsDevelopmentToken";

    private String weiboToken; //用微博账号登录的token
    public static final String WEIBO_TOKEN_KEY = "weiboToken";
    private String renrenToken;//用人人账号登录的token
    public static final String RENREN_TOKEN_KEY = "renrenToken";
    private String qqToken;
    public static final String QQ_TOKEN_KEY = "qqToken";

    //查找好友功能用到的token
    private String findFriendWeiboToken;
    public static final String FIND_FRIEND_WEIBO_TOKEN_KEY = "findFriendWeiboToken";
    private String findFriendWeiboUid;
    public static final String FIND_FRIEND_WEIBO_UID_KEY = "findFriendWeiboUid";

    private Boolean notifyOnReplyPost;//用户帖子被回复时发送通知
    public static final String NOTIFY_ON_REPLY_POST_KEY = "notifyOnReplyPost";
    private Boolean notifyOnFavorite;//用户帖子被喜欢时发送通知
    public static final String NOTIFY_ON_FAVORITE_KEY = "notifyOnFavorite";
    private Boolean notifyOnFollow;//用户被关注时发送通知
    public static final String NOTIFY_ON_FOLLOW_KEY = "notifyOnFollow";
    private Boolean notifySound; //用户设备收到通知时是否有提示音
    public static final String NOTIFY_SOUND_KEY = "notifySound";
    private Boolean notifyVibrator; //用户设备收到通知时是否震动
    public static final String NOTIFY_VIBRATOR_KEY = "notifyVibrator";

    private Integer activeDays;
    public static final String ACTIVE_DAYS_KEY = "activeDays";

    private Date lastActiveDay; //最后活跃日的第一次访问时间
    public static final String LAST_ACTIVE_DAY_KEY = "lastActiveDay";

    @Indexed
    private Robot robot;
    public static final String ROBOT_KEY = "robot";
    public static final String ROBOT_GROUPS_KEY = "robot.groups";

    private Boolean banned;
    public static final String BANNED_KEY = "banned";

    //TODO 临时添加pikeId为了能够正常登录，待上线新服务端稳定后去掉
    private String pikeId;

    public User() {
        initial();
    }

    public User(String id) {
        initial();
        this.id = id;
    }

    private void initial() {
        followersCount = 0;
        friendsCount = 0;
        viewCount = 0;
        beFavoritedCount = 0;
        statusesCount = 0;
        digestCount = 0;
        activeDays = 0;
        notifyOnReplyPost = true;
        notifyOnFavorite = true;
        notifyOnFollow = true;
        notifySound = false;
        notifyVibrator = false;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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
        //TODO 待用户头像全部迁移，删除这个if
        if (profileImageNormal == null) {
            return getProfileImageOriginalUrl();
        }
        return FileContentUrlUtils.getContentUrl(profileImageNormal);
    }

    public String getProfileImage() {
        return profileImageNormal;
    }

    public String getProfileImageBigger() {
        return profileImageBigger;
    }

    public String getProfileImageBiggerUrl() {
        //TODO 待用户头像全部迁移，删除这个if
        if (profileImageBigger == null) {
            return getProfileImageOriginalUrl();
        }
        return FileContentUrlUtils.getContentUrl(profileImageBigger);
    }

    public void setProfileImageBigger(String profileImageBigger) {
        this.profileImageBigger = profileImageBigger;
    }

    public String getProfileImageOriginal() {
        return profileImageOriginal;
    }

    public String getProfileImageOriginalUrl() {
        if (profileImageOriginal == null) {
            return null;
        }
        return FileContentUrlUtils.getContentUrl(profileImageOriginal);
    }

    public void setProfileImageOriginal(String profileImageOriginal) {
        this.profileImageOriginal = profileImageOriginal;
    }

    public Integer getStatusesCount() {
        return statusesCount;
    }

    public void setStatusesCount(Integer statusesCount) {
        this.statusesCount = statusesCount;
    }

    public Integer getLevel() {
        if (digestCount == null) {
            return null;
        }

        int level = 0;
        int count = 0;
        while (count + level + 1 <= digestCount) {
            level++;
            count += level;
            if (level == 60) { //目录最高60级
                return level;
            }
        }
        return level;
    }

    public String getWeiboToken() {
        return weiboToken;
    }

    public void setWeiboToken(String weiboToken) {
        this.weiboToken = weiboToken;
    }

    public String getRenrenToken() {
        return renrenToken;
    }

    public void setRenrenToken(String renrenToken) {
        this.renrenToken = renrenToken;
    }

    public String getFindFriendWeiboToken() {
        return findFriendWeiboToken;
    }

    public void setFindFriendWeiboToken(String findFriendWeiboToken) {
        this.findFriendWeiboToken = findFriendWeiboToken;
    }

    public String getFindFriendWeiboUid() {
        return findFriendWeiboUid;
    }

    public void setFindFriendWeiboUid(String findFriendWeiboUid) {
        this.findFriendWeiboUid = findFriendWeiboUid;
    }

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

    public String getPikeId() {
        return pikeId;
    }

    public void setPikeId(String pikeId) {
        this.pikeId = pikeId;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public String getQqToken() {
        return qqToken;
    }

    public void setQqToken(String qqToken) {
        this.qqToken = qqToken;
    }

    public String getProfileBackgroundImage() {
        return profileBackgroundImage;
    }

    public void setProfileBackgroundImage(String profileBackgroundImage) {
        this.profileBackgroundImage = profileBackgroundImage;
    }

    public String getProfileBackgroundImageUrl() {
        return FileContentUrlUtils.getContentUrl(profileBackgroundImage);
    }

    public Integer getOriginPostsCount() {
        return originPostsCount;
    }

    public void setOriginPostsCount(Integer originPostsCount) {
        this.originPostsCount = originPostsCount;
    }

    public Integer getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(Integer repostsCount) {
        this.repostsCount = repostsCount;
    }

    public Integer getActiveDays() {
        return activeDays;
    }

    public void setActiveDays(Integer activeDays) {
        this.activeDays = activeDays;
    }

    public Date getLastActiveDay() {
        return lastActiveDay;
    }

    public void setLastActiveDay(Date lastActiveDay) {
        this.lastActiveDay = lastActiveDay;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    @Override
    public String toString() {
        return "User{" +
                "screenName='" + screenName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", profileImageNormal='" + profileImageNormal + '\'' +
                ", profileImageBigger='" + profileImageBigger + '\'' +
                ", profileImageOriginal='" + profileImageOriginal + '\'' +
                ", profileBackgroundImage='" + profileBackgroundImage + '\'' +
                ", createdAt=" + createdAt +
                ", verified=" + verified +
                ", weiboVerified=" + weiboVerified +
                ", location='" + location + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", oAuthIds=" + oAuthIds +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", lastSignInAt=" + lastSignInAt +
                ", plainPassword='" + plainPassword + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", roles=" + roles +
                ", followersCount=" + followersCount +
                ", friendsCount=" + friendsCount +
                ", beFavoritedCount=" + beFavoritedCount +
                ", viewCount=" + viewCount +
                ", digestCount=" + digestCount +
                ", statusesCount=" + statusesCount +
                ", originPostsCount=" + originPostsCount +
                ", repostsCount=" + repostsCount +
                ", apnsProductionToken='" + apnsProductionToken + '\'' +
                ", apnsDevelopmentToken='" + apnsDevelopmentToken + '\'' +
                ", weiboToken='" + weiboToken + '\'' +
                ", renrenToken='" + renrenToken + '\'' +
                ", qqToken='" + qqToken + '\'' +
                ", findFriendWeiboToken='" + findFriendWeiboToken + '\'' +
                ", findFriendWeiboUid='" + findFriendWeiboUid + '\'' +
                ", notifyOnReplyPost=" + notifyOnReplyPost +
                ", notifyOnFavorite=" + notifyOnFavorite +
                ", notifyOnFollow=" + notifyOnFollow +
                ", notifySound=" + notifySound +
                ", notifyVibrator=" + notifyVibrator +
                ", activeDays=" + activeDays +
                ", lastActiveDay=" + lastActiveDay +
                ", pikeId='" + pikeId + '\'' +
                "} " + super.toString();
    }
}
