package com.lebo.event;

/**
 * @author: Wei Liu
 * Date: 13-7-29
 * Time: AM11:06
 */
public class AfterDestroyFollowingEvent {
    private String userId;
    private String followingId;

    public AfterDestroyFollowingEvent(String userId, String followingId) {
        this.userId = userId;
        this.followingId = followingId;
    }

    public String getUserId() {
        return userId;
    }

    public String getFollowingId() {
        return followingId;
    }
}
