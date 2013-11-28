package com.lebo.entity;

/**
 * @author: Wei Liu
 * Date: 13-11-5
 * Time: AM11:02
 */
public class UserGoods {
    private String userId;
    private Long goodsId;
    private Integer quantity;

    public UserGoods() {
    }

    public UserGoods(String userId, Long goodsId) {
        this.userId = userId;
        this.goodsId = goodsId;
    }

    public UserGoods(String userId, Long goodsId, int quantity) {
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

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}