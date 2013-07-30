package com.lebo.entity;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 收藏。
 *
 * @author: Wei Liu
 * Date: 13-7-4
 * Time: PM5:50
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "user_favorite_unique", def = "{'userId': 1, 'postId': 1}", unique = true)
})
public class Favorite extends IdEntity {
    public static final String USER_ID_KEY = "userId";
    public static final String POST_ID_KEY = "postId";

    private String userId;
    private String postId;
    @Indexed
    private String postUserId;
    public static final String POST_USERID_KEY = "postUserId";
    @Indexed(direction = IndexDirection.DESCENDING)
    private Date createdAt;
    public static final String CREATED_AT_KEY = "createdAt";

    public Favorite() {
    }

    public Favorite(String userId, String postId, String postUserId) {
        this.userId = userId;
        this.postId = postId;
        this.postUserId = postUserId;
        this.createdAt = new Date();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String postUserId) {
        this.postUserId = postUserId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
