package com.lebo.service.param;

import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-7-15
 * Time: PM8:10
 */
public class StatusFilterParam extends PaginationParam {
    private String follow;
    private String track;
    private String locations;
    private Date after;
    private Boolean digest;

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

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public Date getAfter() {
        return after;
    }

    public void setAfter(Date after) {
        this.after = after;
    }
}
