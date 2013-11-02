package com.lebo.entity;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

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

    public BigDecimal getTotalCost() {
        BigDecimal total = new BigDecimal("0");
        for (OrderDetail orderDetail : orderDetails) {
            total = total.add(orderDetail.getTotalCost());
        }
        return total;
    }

    /**
     * 详细描述, 支付宝接口body参数, 最多300字符
     */
    public String getBody() {
        StringBuilder sb = new StringBuilder();
        for (OrderDetail orderDetail : orderDetails) {
            sb.append(orderDetail.getProduct().getName())
                    .append("×").append(orderDetail.getQuantity()).append(" ");
        }

        return StringUtils.substring(sb.toString().trim(), 0, 300);
    }

    /**
     * 支付宝接口subject参数,最长为 128 个汉字
     */
    public String getSubject() {
        StringBuilder sb = new StringBuilder();
        for (OrderDetail orderDetail : orderDetails) {
            sb.append(orderDetail.getProduct().getProductCategory().getName())
                    .append(" ");
        }

        return StringUtils.substring(sb.toString().trim(), 0, 128);
    }
}
