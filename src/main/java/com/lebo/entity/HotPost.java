package com.lebo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 热门帖子。
 *
 * @author: Wei Liu
 * Date: 13-8-14
 * Time: PM7:32
 */
@Document(collection = "ar.hotposts") //ar - analysis result 的缩写 - 分析结果
public class HotPost extends Post {
    //只存帖子ID

    public HotPost() {
    }

    public HotPost(String id) {
        this.id = id;
    }
}
