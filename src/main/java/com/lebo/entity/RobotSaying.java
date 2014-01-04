package com.lebo.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashSet;

/**
 * @author: Wei Liu
 * Date: 14-1-4
 * Time: PM4:23
 */
@Document(collection = "robot.sayings")
public class RobotSaying extends IdEntity{
    @Indexed(unique = true, dropDups = true)
    private String text;
    public static final String TEXT_KEY = "text";
    @Indexed
    private LinkedHashSet<String> tags;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LinkedHashSet<String> getTags() {
        return tags;
    }

    public void setTags(LinkedHashSet<String> tags) {
        this.tags = tags;
    }
}
