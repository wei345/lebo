package com.lebo.entity;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 热门帖子。
 *
 * @author: Wei Liu
 * Date: 13-8-14
 * Time: PM7:32
 */
@Document(collection = "mr.hotposts")
public class HotPost extends IdEntity {
    @Indexed(direction = IndexDirection.DESCENDING)
    private Integer hotFavoritesCount;
    public static final String HOT_FAVOURITES_COUNT_KEY = "hotFavoritesCount";

    public Integer getHotFavoritesCount() {
        return hotFavoritesCount;
    }

    public void setHotFavoritesCount(Integer hotFavoritesCount) {
        this.hotFavoritesCount = hotFavoritesCount;
    }

    @Override
    public String toString() {
        return "HotPost{" +
                "hotFavoritesCount=" + hotFavoritesCount +
                "} " + super.toString();
    }
}
