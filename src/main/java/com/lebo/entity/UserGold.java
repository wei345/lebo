package com.lebo.entity;

/**
 * @author: Wei Liu
 * Date: 13-11-4
 * Time: PM4:46
 */
public class UserGold {
    private String userId;
    private Integer goldQuantity;

    public UserGold(){

    }

    public UserGold(String userId, Integer goldQuantity){
        this.userId = userId;
        this.goldQuantity = goldQuantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getGoldQuantity() {
        return goldQuantity;
    }

    public void setGoldQuantity(Integer goldQuantity) {
        this.goldQuantity = goldQuantity;
    }
}
