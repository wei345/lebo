package com.lebo.entity;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单
 *
 * @author: Wei Liu
 * Date: 13-10-30
 * Time: PM3:03
 */
public class Order {
    private Long orderId;
    private String mongoUserId;
    private Date orderDate;
    private BigDecimal discount;
    private Status status;

    private List<OrderDetail> orderDetails = Lists.newArrayList();

    public static enum Status {
        PAID("已支付"),
        UNPAID("未支付"),
        OBSOLETE("已作废");

        Status(String description) {
        }
    }

    public Order() {
    }

    public Order(String mongoUserId, BigDecimal discount, Status status) {
        this.mongoUserId = mongoUserId;
        this.discount = discount;
        this.status = status;
        this.orderDate = new Date();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMongoUserId() {
        return mongoUserId;
    }

    public void setMongoUserId(String mongoUserId) {
        this.mongoUserId = mongoUserId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
