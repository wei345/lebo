package com.lebo.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lebo.entity.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-22
 * Time: PM12:38
 */
public class SettingDto {
    private List<ChannelDto> channels = new ArrayList<ChannelDto>();
    private String officialAccountId;

    public List<ChannelDto> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelDto> channels) {
        this.channels = channels;
    }

    public String getOfficialAccountId() {
        return officialAccountId;
    }

    public void setOfficialAccountId(String officialAccountId) {
        this.officialAccountId = officialAccountId;
    }

    public static class ChannelDto{
        private String name;
        private String description;
        private String contentUrl;
        private String imageUrl;
        private String image;
        private String backgroundColor;

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

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @JsonIgnore
        public String getImage() {
            return image;
        }
    }
}
