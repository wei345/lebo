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
public class RecommendedApp extends IdEntity{
    private String name;
    private String url;
    private Boolean directDownload;  //url是否是直接下载地址
    private String description;
    private String imageKey;
    private String imageSlug;
    private String backgroundColor;
    private String version;
    private String size;
    private int order;
    public static final String ORDER_KEY = "order";
    private boolean enabled;
    public static final String ENABLED_KEY = "enabled";
    private String type;
    public static final String TYPE_KEY = "type";
    public static final String TYPE_IOS = "ios";
    public static final String TYPE_ANDROID = "android";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getImageUrl() {
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

    public String getImageSlug() {
        return imageSlug;
    }

    public void setImageSlug(String imageSlug) {
        this.imageSlug = imageSlug;
    }

    public Boolean getDirectDownload() {
        return directDownload;
    }

    public void setDirectDownload(Boolean directDownload) {
        this.directDownload = directDownload;
    }
}
