package com.lebo.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 我的粉丝。
 *
 * @author: Wei Liu
 * Date: 13-7-4
 * Time: PM5:35
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "user_follower_unique", def = "{'userId': 1, 'followerId': 1}", unique = true)
})
public class Follower extends IdEntity {
    private String userId;
    private String followerId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }
}
