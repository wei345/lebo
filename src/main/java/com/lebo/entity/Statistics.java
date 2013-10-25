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
    private Long totalUserCount;
    private Long adminUserCount;

    private Long totalPostCount;
    private Long originPostCount;
    private Long repostPostCount;

    private Long totalCommentCount;
    private Long audioCommentCount;
    private Long videoCommentCount;

    private Long totalFavoriteCount;
    private Long totalFollowCount;
    private Long totalFriendshipCount;
    private Long hashtagCount;
    private Long notificationCount;
    private Long quartzJobCount;

    private Date createdAt;
    private Long collectTimeMillis; //本次收集数据花费的时间，毫秒

    public Long getTotalUserCount() {
        return totalUserCount;
    }

    public void setTotalUserCount(Long totalUserCount) {
        this.totalUserCount = totalUserCount;
    }

    public Long getAdminUserCount() {
        return adminUserCount;
    }

    public void setAdminUserCount(Long adminUserCount) {
        this.adminUserCount = adminUserCount;
    }

    public Long getTotalPostCount() {
        return totalPostCount;
    }

    public void setTotalPostCount(Long totalPostCount) {
        this.totalPostCount = totalPostCount;
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

    public Long getTotalCommentCount() {
        return totalCommentCount;
    }

    public void setTotalCommentCount(Long totalCommentCount) {
        this.totalCommentCount = totalCommentCount;
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

    public Long getTotalFavoriteCount() {
        return totalFavoriteCount;
    }

    public void setTotalFavoriteCount(Long totalFavoriteCount) {
        this.totalFavoriteCount = totalFavoriteCount;
    }

    public Long getTotalFollowCount() {
        return totalFollowCount;
    }

    public void setTotalFollowCount(Long totalFollowCount) {
        this.totalFollowCount = totalFollowCount;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCollectTimeMillis() {
        return collectTimeMillis;
    }

    public void setCollectTimeMillis(Long collectTimeMillis) {
        this.collectTimeMillis = collectTimeMillis;
    }

    public Long getTotalFriendshipCount() {
        return totalFriendshipCount;
    }

    public void setTotalFriendshipCount(Long totalFriendshipCount) {
        this.totalFriendshipCount = totalFriendshipCount;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "totalUserCount=" + totalUserCount +
                ", adminUserCount=" + adminUserCount +
                ", totalPostCount=" + totalPostCount +
                ", originPostCount=" + originPostCount +
                ", repostPostCount=" + repostPostCount +
                ", totalCommentCount=" + totalCommentCount +
                ", audioCommentCount=" + audioCommentCount +
                ", videoCommentCount=" + videoCommentCount +
                ", totalFavoriteCount=" + totalFavoriteCount +
                ", totalFollowCount=" + totalFollowCount +
                ", totalFriendshipCount=" + totalFriendshipCount +
                ", hashtagCount=" + hashtagCount +
                ", notificationCount=" + notificationCount +
                ", quartzJobCount=" + quartzJobCount +
                ", createdAt=" + createdAt +
                ", collectTimeMillis=" + collectTimeMillis +
                "} " + super.toString();
    }
}
