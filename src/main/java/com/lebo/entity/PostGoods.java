package com.lebo.entity;

/**
 * @author: Wei Liu
 * Date: 13-12-12
 * Time: PM3:07
 */
public class PostGoods {
    private String postId;
    private Integer goodsId;
    private Integer quantity;

    public PostGoods() {
    }

    public PostGoods(String postId, Integer goodsId) {
        this.postId = postId;
        this.goodsId = goodsId;
    }

    public PostGoods(String postId, Integer goodsId, int quantity) {
        this.postId = postId;
        this.goodsId = goodsId;
        this.quantity = quantity;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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
