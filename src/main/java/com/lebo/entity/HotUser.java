package com.lebo.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 热门用户。(即："推荐关注")
 *
 * @author: Wei Liu
 * Date: 13-8-15
 * Time: PM3:58
 */
@Document(collection = "mr.hotusers")
public class HotUser extends IdEntity {
    @Field("value")
    private Integer hotBeFavoritedCount;
    public static final String HOT_BE_FAVORITED_COUNT = "value";

    public Integer getHotBeFavoritedCount() {
        return hotBeFavoritedCount;
    }

    public void setHotBeFavoritedCount(Integer hotBeFavoritedCount) {
        this.hotBeFavoritedCount = hotBeFavoritedCount;
    }

    @Override
    public String toString() {
        return "HotUser{" +
                "hotBeFavoritedCount=" + hotBeFavoritedCount +
                "} " + super.toString();
    }
}
