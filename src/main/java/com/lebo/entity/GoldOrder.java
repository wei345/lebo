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
    private String userId;
    private GoldProduct goldProduct;
    private Integer quantity;
    private BigDecimal discount;
    private Date orderDate;
    private Status status;
    private PaymentMethod paymentMethod;
    private String alipayStatus;
    private String alipayNotifyId;

    public static enum Status {
        PAID("已支付"),
        UNPAID("未支付"),
        OBSOLETE("已作废");

        Status(String description) {
        }
    }

    public static enum PaymentMethod {
        ALIPAY("支付宝");

        private String name;

        PaymentMethod(String name) {
            this.name = name;
        }
    }

    public GoldOrder() {
    }

    public GoldOrder(String userId, BigDecimal discount, Status status, PaymentMethod paymentMethod) {
        this.userId = userId;
        this.discount = discount;
        this.status = status;
        this.orderDate = new Date();
        this.paymentMethod = paymentMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
    public String getAlipayBody() {
        return StringUtils.substring(
                new StringBuilder(goldProduct.getName())
                        .append("×").append(quantity).toString(), 0, 300);
    }

    /**
     * 支付宝接口subject参数,最长为 128 个汉字
     */
    public String getAlipaySubject() {
        return StringUtils.substring(
                new StringBuilder(goldProduct.getName())
                        .append("×").append(quantity).toString(), 0, 128);
    }

    public String getAlipayStatus() {
        return alipayStatus;
    }

    public void setAlipayStatus(String alipayStatus) {
        this.alipayStatus = alipayStatus;
    }

    public Long getGoldQuantity() {
        return goldProduct.getGoldQuantity() * quantity;
    }

    public String getAlipayNotifyId() {
        return alipayNotifyId;
    }

    public void setAlipayNotifyId(String alipayNotifyId) {
        this.alipayNotifyId = alipayNotifyId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
