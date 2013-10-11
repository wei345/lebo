package com.lebo.rest.dto;

import java.util.List;

/**
 * 热门.
 *
 * @author: Wei Liu
 * Date: 13-10-11
 * Time: PM12:17
 */
public class HotDto {
    private List<AdDto> ads;
    private List<UserDto> users;

    public List<AdDto> getAds() {
        return ads;
    }

    public void setAds(List<AdDto> ads) {
        this.ads = ads;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}
