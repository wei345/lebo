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
public class Option extends IdEntity {
    private List<Channel> channels = new ArrayList<Channel>();

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public static class Channel{
        private String name;
        //statuses/filter参数
        private String content;
        //图片存在MongoDB中，图片可在后台修改
        private String image;
        private String backgroundColor;
        private boolean enabled;

        public Channel(){

        }

        public Channel(String name, String content, String image, String backgroundColor, boolean enabled) {
            this.name = name;
            this.content = content;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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
