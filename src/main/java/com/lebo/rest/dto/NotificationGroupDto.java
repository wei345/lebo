package com.lebo.rest.dto;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-10-26
 * Time: PM4:08
 */
public class NotificationGroupDto {
    private String groupName;
    private List<String> activityTypes;
    private Integer unreadCount;
    private List<NotificationDto> recentNotifications;

    public List<String> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(List<String> activityTypes) {
        this.activityTypes = activityTypes;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public List<NotificationDto> getRecentNotifications() {
        return recentNotifications;
    }

    public void setRecentNotifications(List<NotificationDto> recentNotifications) {
        this.recentNotifications = recentNotifications;
    }
}
