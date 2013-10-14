package com.lebo.entity;

import com.lebo.service.FileContentUrlUtils;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 推荐的应用。
 *
 * @author: Wei Liu
 * Date: 13-10-14
 * Time: PM3:48
 */
@Document(collection = "settings.apps")
public class RecommendedApp {
    private String name;
    private String iosUrl;
    public static final String IOS_URL_KEY = "iosUrl";
    private String androidUrl;
    public static final String ANDROID_URL_KEY = "androidUrl";
    private String description;
    private String imageKey;
    private String backgroundColor;
    private String version;
    private String size;
    private int order;
    public static final String ORDER_KEY = "order";
    private boolean enabled;
    public static final String ENABLED_KEY = "enabled";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIosUrl() {
        return iosUrl;
    }

    public void setIosUrl(String iosUrl) {
        this.iosUrl = iosUrl;
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getImageUrl(){
        return FileContentUrlUtils.getContentUrl(imageKey);
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
