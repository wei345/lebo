package com.lebo.entity;

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
    //精华。由乐播官方账号转发，最近多少天的内容
    private Integer bestContentDays;

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

    public Integer getBestContentDays() {
        return bestContentDays;
    }

    public void setBestContentDays(Integer bestContentDays) {
        this.bestContentDays = bestContentDays;
    }

    public static class Channel{
        private String name;
        //statuses/filter参数
        private String contentUrl;
        //图片存在MongoDB中，图片可在后台修改
        private String image;
        private String backgroundColor;
        private boolean enabled;

        public Channel(){

        }

        public Channel(String name, String contentUrl, String image, String backgroundColor, boolean enabled) {
            this.name = name;
            this.contentUrl = contentUrl;
            this.image = image;
            this.backgroundColor = backgroundColor;
            this.enabled = enabled;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
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
    }
}
