package com.lebo.entity;

import com.lebo.service.FileContentUrlUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "settings.channels")
public class Channel extends IdEntity {
    @Indexed
    private String name;
    public static final String NAME_KEY = "name";
    private String title;
    private String description;
    private String image;
    private String backgroundColor;
    private boolean enabled;
    public static final String ENABLED_KEY = "enabled";
    private String follow;
    private String track;
    private Integer postsCount;
    private Integer favoritesCount;
    private Integer viewCount;
    private String slug;
    private int order;//小在前，大在后
    public static final String ORDER_KEY = "order";
    //TODO 让新版客户端做banner功能，去掉置顶视频
    private String topPostId; //该频道置顶视频

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public Integer getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(Integer postsCount) {
        this.postsCount = postsCount;
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

    public String getImageUrl() {
        return FileContentUrlUtils.getContentUrl(getImage());
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTopPostId() {
        return topPostId;
    }

    public void setTopPostId(String topPostId) {
        this.topPostId = topPostId;
    }
}