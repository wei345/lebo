package com.lebo.entity;

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
    private String userMongoId;

    private Date orderDate;
    private BigDecimal total;
    private BigDecimal discount;
    private Status status;

    private String outTradeNo;
    private String subject;
    private BigDecimal totalFee;
    private String body;

    private List<OrderDetail> orderDetailList;

    public static enum Status {
        PAID("已支付"),
        UNPAID("未支付"),
        OBSOLETE("已作废");

        Status(String description) {
        }
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUserMongoId() {
        return userMongoId;
    }

    public void setUserMongoId(String userMongoId) {
        this.userMongoId = userMongoId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
}
