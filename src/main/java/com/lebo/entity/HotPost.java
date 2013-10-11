package com.lebo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

/**
 * 热门帖子。
 *
 * @author: Wei Liu
 * Date: 13-8-14
 * Time: PM7:32
 */
@Document(collection = "ar.hotposts") //ar - analysis result 的缩写 - 分析结果
public class HotPost {
    @Id
    private int id;
    public static final String ID_KEY = "_id";
    private String postId;
    public static final String POST_ID_KEY = "postId";


    public HotPost() {
    }

    public HotPost(int id, String postId) {
        this.id = id;
        this.postId = postId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
