package com.lebo.entity;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单
 *
 * @author: Wei Liu
 * Date: 13-10-30
 * Time: PM3:03
 */
public class GoldOrder {
    private Long id;
    private String mongoUserId;
    private GoldProduct goldProduct;
    private Integer quantity;
    private BigDecimal discount;
    private Date orderDate;
    private Status status;


    public static enum Status {
        PAID("已支付"),
        UNPAID("未支付"),
        OBSOLETE("已作废");

        Status(String description) {
        }
    }

    public GoldOrder() {
    }

    public GoldOrder(String mongoUserId, BigDecimal discount, Status status) {
        this.mongoUserId = mongoUserId;
        this.discount = discount;
        this.status = status;
        this.orderDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public GoldProduct getGoldProduct() {
        return goldProduct;
    }

    public void setGoldProduct(GoldProduct goldProduct) {
        this.goldProduct = goldProduct;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalCost() {
        return goldProduct.getCost().multiply(new BigDecimal(quantity)).add(discount);
    }

    /**
     * 详细描述, 支付宝接口body参数, 最多300字符
     */
    public String getBody() {
        return StringUtils.substring(
                new StringBuilder(goldProduct.getName())
                        .append("×").append(quantity).toString(), 0, 300);
    }

    /**
     * 支付宝接口subject参数,最长为 128 个汉字
     */
    public String getSubject() {
        return StringUtils.substring(
                new StringBuilder(goldProduct.getName())
                        .append("×").append(quantity).toString(), 0, 128);
    }
}
