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
@Document(collection = "settings")
public class Setting extends IdEntity {
    private String officialAccountId;

    private Integer digestDays = 2;
    private Integer hotDays = 2;

    private String appStoreLeboUrl = "https://itunes.apple.com/cn/app/le-bo-6miao-shi-pin/id598266288?mt=8";
    private String leboAppAndroidDownloadUrl = "http://www.lebooo.com/lebo_1.1_20130802.apk";

    //红人榜 -> 粉丝最多
    private String hotuser_button1_backgroundColor;
    private String hotuser_button1_imageKey;
    private String hotuser_button1_text;

    //红人榜 -> 最受喜欢
    private String hotuser_button2_backgroundColor;
    private String hotuser_button2_imageKey;
    private String hotuser_button2_text;

    //红人榜 -> 导演排行
    private String hotuser_button3_backgroundColor;
    private String hotuser_button3_imageKey;
    private String hotuser_button3_text;

    public String getOfficialAccountId() {
        return officialAccountId;
    }

    public void setOfficialAccountId(String officialAccountId) {
        this.officialAccountId = officialAccountId;
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

    public String getAppStoreLeboUrl() {
        return appStoreLeboUrl;
    }

    public void setAppStoreLeboUrl(String appStoreLeboUrl) {
        this.appStoreLeboUrl = appStoreLeboUrl;
    }

    public String getLeboAppAndroidDownloadUrl() {
        return leboAppAndroidDownloadUrl;
    }

    public void setLeboAppAndroidDownloadUrl(String leboAppAndroidDownloadUrl) {
        this.leboAppAndroidDownloadUrl = leboAppAndroidDownloadUrl;
    }

    public static class HotUserButton{
        private String backgroundColor;
        private String imageKey;
        private String text;

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getImageKey() {
            return imageKey;
        }

        public void setImageKey(String imageKey) {
            this.imageKey = imageKey;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
