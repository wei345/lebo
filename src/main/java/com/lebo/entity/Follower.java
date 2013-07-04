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
    @CompoundIndex(name = "my_follower", def = "{'myId': 1, 'followerId': 1}", unique = true)
})
public class Follower extends IdEntity {
    private String myId;
    private String followerId;

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }
}
