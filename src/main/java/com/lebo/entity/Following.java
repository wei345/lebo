package com.lebo.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 我的关注。
 *
 * @author: Wei Liu
 * Date: 13-7-4
 * Time: PM5:39
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "user_following_unique", def = "{'userId': 1, 'followingId': 1}", unique = true)
})
public class Following extends IdEntity {
    private String userId;
    @Indexed
    private String followingId;

    public Following() {
    }

    public Following(String userId, String followingId) {
        this.userId = userId;
        this.followingId = followingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }
}
