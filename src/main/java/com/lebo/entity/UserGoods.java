package com.lebo.entity;

/**
 * @author: Wei Liu
 * Date: 13-11-5
 * Time: AM11:02
 */
public class UserGoods {
    private String userId;
    private Integer goodsId;
    private Integer quantity;

    public UserGoods() {
    }

    public UserGoods(String userId, Integer goodsId) {
        this.userId = userId;
        this.goodsId = goodsId;
    }

    public UserGoods(String userId, Integer goodsId, int quantity) {
        this.userId = userId;
        this.goodsId = goodsId;
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
