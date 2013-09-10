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

    //TODO 乐播app下载地址可配置
    private String appStoreLeboUrl = "https://itunes.apple.com/cn/app/le-bo-6miao-shi-pin/id598266288?mt=8";
    private String leboAppAndroidDownloadUrl = "http://www.lebooo.com/lebo_1.1_20130802.apk";

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
}
