package com.lebo.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 喜欢。
 *
 * @author: Wei Liu
 * Date: 13-7-4
 * Time: PM5:50
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "my_favorite", def = "{'userId': 1, 'tweetId': 1}", unique = true)
})
public class Favorite extends IdEntity {
    private String userId;
    private String tweetId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }
}
