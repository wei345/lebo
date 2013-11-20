package com.lebo.entity;

import com.lebo.service.FileContentUrlUtils;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 广告
 *
 * @author: Wei Liu
 * Date: 13-10-11
 * Time: PM4:03
 */
@Document(collection = "ads")
public class Ad extends IdEntity {
    private String imageKey;
    private String subject;
    private String description;
    private String url;
    private int order;//小在前，大在后
    public static final String ORDER_KEY = "order";
    private String group;
    public static final String GROUP_KEY = "group";
    public static final String GROUP_HOT = "hot";
    private boolean enabled;
    public static final String ENABLED_KEY = "enabled";

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getImageUrl() {
        return FileContentUrlUtils.getContentUrl(imageKey);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
