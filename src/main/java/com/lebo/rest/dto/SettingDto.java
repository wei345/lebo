package com.lebo.rest.dto;

/**
 * @author: Wei Liu
 * Date: 13-7-22
 * Time: PM12:38
 */
public class SettingDto {
    private String officialAccountId;
    private String digestAccountId;

    public String getOfficialAccountId() {
        return officialAccountId;
    }

    public void setOfficialAccountId(String officialAccountId) {
        this.officialAccountId = officialAccountId;
    }

    public String getDigestAccountId() {
        return digestAccountId;
    }

    public void setDigestAccountId(String digestAccountId) {
        this.digestAccountId = digestAccountId;
    }
}
