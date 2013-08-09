package com.lebo.event;

/**
 * @author: Wei Liu
 * Date: 13-7-29
 * Time: AM11:02
 */
public class AfterFollowingCreatEvent {
    private String userId;
    private String followingId;

    public AfterFollowingCreatEvent(String userId, String followingId) {
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
