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
@Document
public class Hashtag extends IdEntity{
    @Indexed(unique = true)
    private String name;
    public static final String NAME_KEY = "name";
    @Indexed(direction = IndexDirection.DESCENDING)
    private Integer count;
    public static final String COUNT_KEY = "count";
    private Date increaseAt;
    /**
     *  count增长时间，也就是最后使用该hashtag的post的时间。
     *  可认为是该hashtag最后活跃时间。
     */
    public static final String INCREASE_AT_KEY = "increaseAt";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getIncreaseAt() {
        return increaseAt;
    }

    public void setIncreaseAt(Date increaseAt) {
        this.increaseAt = increaseAt;
    }
}
