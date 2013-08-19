package com.lebo.entity;

import com.lebo.service.FileContentUrlUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用选项。
 *
 * @author: Wei Liu
 * Date: 13-7-18
 * Time: PM4:18
 */
@Document
public class Setting extends IdEntity {
    private List<Channel> channels = new ArrayList<Channel>();
    private String officialAccountId;

    private Integer digestDays = 2;
    private Integer hotDays = 2;

    public String getOfficialAccountId() {
        return officialAccountId;
    }

    public void setOfficialAccountId(String officialAccountId) {
        this.officialAccountId = officialAccountId;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public Integer getDigestDays() {
        return digestDays;
    }

    public void setDigestDays(Integer digestDays) {
        this.digestDays = digestDays;
    }

    public Integer getHotDays() {
        return hotDays;
    }

    public void setHotDays(Integer hotDays) {
        this.hotDays = hotDays;
    }

    public static class Channel {
        private String name;
        private String title;
        private String description;
        private String image;
        private String backgroundColor;
        private boolean enabled;
        private String follow;
        private String track;
        private Integer postsCount;
        private Integer favoritesCount;
        private Integer viewCount;

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

        public String getImageUrl(){
            return FileContentUrlUtils.getContentUrl(getImage());
        }
    }
}
