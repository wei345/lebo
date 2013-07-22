package com.lebo.rest.dto;

import com.lebo.entity.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-22
 * Time: PM12:38
 */
public class OptionDto {
    private List<Option.Channel> channels = new ArrayList<Option.Channel>();
    private String officialAccountId;
    private String bestContentUrl;

    public List<Option.Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Option.Channel> channels) {
        this.channels = channels;
    }

    public String getOfficialAccountId() {
        return officialAccountId;
    }

    public void setOfficialAccountId(String officialAccountId) {
        this.officialAccountId = officialAccountId;
    }

    public String getBestContentUrl() {
        return bestContentUrl;
    }

    public void setBestContentUrl(String bestContentUrl) {
        this.bestContentUrl = bestContentUrl;
    }
}
