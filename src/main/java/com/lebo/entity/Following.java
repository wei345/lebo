package com.lebo.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
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
        @CompoundIndex(name = "my_following", def = "{'myId': 1, 'followingId': 1}", unique = true)
})
public class Following extends IdEntity{
    private String myId;
    private String followingId;

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }
}
