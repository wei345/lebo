package com.lebo.entity;

import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-11-5
 * Time: PM2:55
 */
public class GiveGoods {
    private Long id;
    private String fromUserId;
    private String toUserId;
    private Integer goodsId;
    private Integer quantity;
    private String postId;
    private Date giveDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
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

    public Date getGiveDate() {
        return giveDate;
    }

    public void setGiveDate(Date giveDate) {
        this.giveDate = giveDate;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
