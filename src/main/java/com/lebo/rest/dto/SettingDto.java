package com.lebo.rest.dto;

import com.lebo.entity.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-22
 * Time: PM12:38
 */
public class SettingDto {
    private List<Setting.Channel> channels = new ArrayList<Setting.Channel>();
    private String officialAccountId;

    public List<Setting.Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Setting.Channel> channels) {
        this.channels = channels;
    }

    public String getOfficialAccountId() {
        return officialAccountId;
    }

    public void setOfficialAccountId(String officialAccountId) {
        this.officialAccountId = officialAccountId;
    }
}
