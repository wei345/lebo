package com.lebo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author: Wei Liu
 * Date: 13-7-4
 * Time: PM4:34
 */
@Document
public class Reply extends Tweet {
    private String tweetId;

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }
}
