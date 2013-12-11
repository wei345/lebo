package com.lebo.rest.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-11-4
 * Time: PM7:14
 */
public class UserVgDto {
    private String userId;
    private String screenName;
    private String profileImageUrl;
    private String profileImageBiggerUrl;
    private String profileImageOriginalUrl;
    private Integer gold;
    private Integer consumeGold;
    private List<UserGoodsDto> goods = new ArrayList<UserGoodsDto>();
    private Integer goodsTotalPrice;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageBiggerUrl() {
        return profileImageBiggerUrl;
    }

    public void setProfileImageBiggerUrl(String profileImageBiggerUrl) {
        this.profileImageBiggerUrl = profileImageBiggerUrl;
    }

    public String getProfileImageOriginalUrl() {
        return profileImageOriginalUrl;
    }

    public void setProfileImageOriginalUrl(String profileImageOriginalUrl) {
        this.profileImageOriginalUrl = profileImageOriginalUrl;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public List<UserGoodsDto> getGoods() {
        return goods;
    }

    public void setGoods(List<UserGoodsDto> goods) {
        this.goods = goods;
    }

    public Integer getGoodsTotalPrice() {
        return goodsTotalPrice;
    }

    public void setGoodsTotalPrice(Integer goodsTotalPrice) {
        this.goodsTotalPrice = goodsTotalPrice;
    }

    public Integer getConsumeGold() {
        return consumeGold;
    }

    public void setConsumeGold(Integer consumeGold) {
        this.consumeGold = consumeGold;
    }
}
