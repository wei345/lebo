package com.lebo.rest.dto;

import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-9-9
 * Time: PM6:21
 */
public class CheckVersionDto {
    private Boolean forceUpgrade; //根据客户端当前版本判断是否必须升级
    private String downloadUrl; //最新版客户端下载地址
    private String version; //最新版客户端版本号
    private Date releaseAt; //最新版客户端发布日期
    private String releaseNotes; //最新版客户端发行说明

    public Boolean getForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(Boolean forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getReleaseAt() {
        return releaseAt;
    }

    public void setReleaseAt(Date releaseAt) {
        this.releaseAt = releaseAt;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }
}
