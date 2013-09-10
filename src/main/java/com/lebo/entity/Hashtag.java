package com.lebo.entity;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-7-31
 * Time: PM3:48
 */
@Document(collection = "hashtags")
public class Hashtag extends IdEntity {
    @Indexed(unique = true)
    private String name;
    public static final String NAME_KEY = "name";
    @Indexed(direction = IndexDirection.DESCENDING)
    private Integer postsCount;
    public static final String POSTS_COUNT_KEY = "postsCount";
    private Integer favoritesCount;
    public static final String FAVORITES_COUNT_KEY = "favoritesCount";
    private Integer viewCount;
    public static final String VIEW_COUNT_KEY = "viewCount";
    /**
     * count增长时间，也就是最后使用该hashtag的post的时间。
     * 可认为是该hashtag最后活跃时间。
     */
    private Date increaseAt;
    public static final String INCREASE_AT_KEY = "increaseAt";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(Integer postsCount) {
        this.postsCount = postsCount;
    }

    public Date getIncreaseAt() {
        return increaseAt;
    }

    public void setIncreaseAt(Date increaseAt) {
        this.increaseAt = increaseAt;
    }

    public Integer getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(Integer favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
}
