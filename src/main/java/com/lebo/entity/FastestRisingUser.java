package com.lebo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 上升最快用户.
 *
 * @author: Wei Liu
 * Date: 13-10-22
 * Time: PM6:14
 */
@Document(collection = "ar.frusers") //ar - analysis result, frusers - fastest rising users
public class FastestRisingUser extends IdEntity {
    private String value;
    public static final String VALUE_KEY = "value";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
