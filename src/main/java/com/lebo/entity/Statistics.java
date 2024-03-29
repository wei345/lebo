package com.lebo.entity;

import java.util.Date;

/**
 * 统计.
 *
 * @author: Wei Liu
 * Date: 13-10-25
 * Time: PM2:36
 */
public class Statistics extends IdEntity{
    public static final String COLLECTION_STATISTICS = "statistics"; //统计总数
    public static final String COLLECTION_STATISTICS_DAILY = "statistics.daily"; //统计每天数据

    private Long userCount;
    private Long adminUserCount;

    private Long postCount;
    private Long originPostCount;
    private Long repostPostCount;

    private Long commentCount;
    private Long audioCommentCount;
    private Long videoCommentCount;

    private Long favoriteCount;

    private Long followCount;
    private Long friendshipCount;

    private Long hashtagCount;

    private Long notificationCount;

    private Long quartzJobCount;

    private Long imCount;
    private Long imTextCount;
    private Long imVideoCount;
    private Long imAudioCount;
    private Long imFromUserCount;

    private Date statisticsDate;
    public static final String STATISTICS_DATE_KEY = "statisticsDate";
    private Long collectTimeMillis; //本次收集数据花费的时间，毫秒

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public Long getAdminUserCount() {
        return adminUserCount;
    }

    public void setAdminUserCount(Long adminUserCount) {
        this.adminUserCount = adminUserCount;
    }

    public Long getPostCount() {
        return postCount;
    }

    public void setPostCount(Long postCount) {
        this.postCount = postCount;
    }

    public Long getOriginPostCount() {
        return originPostCount;
    }

    public void setOriginPostCount(Long originPostCount) {
        this.originPostCount = originPostCount;
    }

    public Long getRepostPostCount() {
        return repostPostCount;
    }

    public void setRepostPostCount(Long repostPostCount) {
        this.repostPostCount = repostPostCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Long getAudioCommentCount() {
        return audioCommentCount;
    }

    public void setAudioCommentCount(Long audioCommentCount) {
        this.audioCommentCount = audioCommentCount;
    }

    public Long getVideoCommentCount() {
        return videoCommentCount;
    }

    public void setVideoCommentCount(Long videoCommentCount) {
        this.videoCommentCount = videoCommentCount;
    }

    public Long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Long getFollowCount() {
        return followCount;
    }

    public void setFollowCount(Long followCount) {
        this.followCount = followCount;
    }

    public Long getHashtagCount() {
        return hashtagCount;
    }

    public void setHashtagCount(Long hashtagCount) {
        this.hashtagCount = hashtagCount;
    }

    public Long getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(Long notificationCount) {
        this.notificationCount = notificationCount;
    }

    public Long getQuartzJobCount() {
        return quartzJobCount;
    }

    public void setQuartzJobCount(Long quartzJobCount) {
        this.quartzJobCount = quartzJobCount;
    }

    public Long getCollectTimeMillis() {
        return collectTimeMillis;
    }

    public void setCollectTimeMillis(Long collectTimeMillis) {
        this.collectTimeMillis = collectTimeMillis;
    }

    public Long getFriendshipCount() {
        return friendshipCount;
    }

    public void setFriendshipCount(Long friendshipCount) {
        this.friendshipCount = friendshipCount;
    }

    public Date getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Date statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public Long getImCount() {
        return imCount;
    }

    public void setImCount(Long imCount) {
        this.imCount = imCount;
    }

    public Long getImTextCount() {
        return imTextCount;
    }

    public void setImTextCount(Long imTextCount) {
        this.imTextCount = imTextCount;
    }

    public Long getImVideoCount() {
        return imVideoCount;
    }

    public void setImVideoCount(Long imVideoCount) {
        this.imVideoCount = imVideoCount;
    }

    public Long getImAudioCount() {
        return imAudioCount;
    }

    public void setImAudioCount(Long imAudioCount) {
        this.imAudioCount = imAudioCount;
    }

    public Long getImFromUserCount() {
        return imFromUserCount;
    }

    public void setImFromUserCount(Long imFromUserCount) {
        this.imFromUserCount = imFromUserCount;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "userCount=" + userCount +
                ", adminUserCount=" + adminUserCount +
                ", postCount=" + postCount +
                ", originPostCount=" + originPostCount +
                ", repostPostCount=" + repostPostCount +
                ", commentCount=" + commentCount +
                ", audioCommentCount=" + audioCommentCount +
                ", videoCommentCount=" + videoCommentCount +
                ", favoriteCount=" + favoriteCount +
                ", followCount=" + followCount +
                ", friendshipCount=" + friendshipCount +
                ", hashtagCount=" + hashtagCount +
                ", notificationCount=" + notificationCount +
                ", quartzJobCount=" + quartzJobCount +
                ", imCount=" + imCount +
                ", imTextCount=" + imTextCount +
                ", imVideoCount=" + imVideoCount +
                ", imAudioCount=" + imAudioCount +
                ", imFromUserCount=" + imFromUserCount +
                ", statisticsDate=" + statisticsDate +
                ", collectTimeMillis=" + collectTimeMillis +
                "} " + super.toString();
    }
}
