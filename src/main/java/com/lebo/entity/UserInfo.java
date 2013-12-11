package com.lebo.entity;

import java.math.BigDecimal;

/**
 * @author: Wei Liu
 * Date: 13-11-4
 * Time: PM4:46
 */
public class UserInfo {
    private String userId;
    private Integer gold;
    private Integer consumeGold;
    private BigDecimal recharge;
    private Integer popularity; //人气

    public UserInfo() {

    }

    public UserInfo(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Integer getConsumeGold() {
        return consumeGold;
    }

    public void setConsumeGold(Integer consumeGold) {
        this.consumeGold = consumeGold;
    }

    public BigDecimal getRecharge() {
        return recharge;
    }

    public void setRecharge(BigDecimal recharge) {
        this.recharge = recharge;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }
}
