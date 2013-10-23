package com.lebo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Top 50用户，一周内用户获得的红心数排名
 */
@Document(collection = "mr.top50users")
public class Top50User extends IdEntity {
    private String value;
    public static final String VALUE_KEY = "value";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}