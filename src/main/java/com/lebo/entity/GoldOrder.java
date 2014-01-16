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
    private String id;
    private String userId;
    private GoldProduct goldProduct;
    private Integer quantity;
    private BigDecimal discount;
    private String subject;        //购买内容描述
    private BigDecimal totalCost;  //折扣之后用户需支付的费用
    private Integer gold;  //需交付给用户的金币数
    private Date orderDate;
    private Status status;
    private PaymentMethod paymentMethod;
    private String paymentStatus;
    private String paymentDetail;

    public static enum Status {
        PAID,        //已支付
        UNPAID,      //未支付
        OBSOLETE;    //已作废
    }

    public static enum PaymentMethod {
        ALIPAY,
        IN_APP_PURCHASE
    }

    public GoldOrder() {
    }

    public GoldOrder(String id) {
        this.id = id;
    }

    public GoldOrder(String id, String userId, BigDecimal discount, Status status, PaymentMethod paymentMethod) {
        this.id = id;
        this.userId = userId;
        this.discount = discount;
        this.status = status;
        this.orderDate = new Date();
        this.paymentMethod = paymentMethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    /**
     * 详细描述, 支付宝接口body参数, 最多300字符
     */
    public String getAlipayBody() {
        return StringUtils.substring(subject, 0, 300);
    }

    /**
     * 支付宝接口subject参数,最长为 128 个汉字
     */
    public String getAlipaySubject() {
        return StringUtils.substring(subject, 0, 128);
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Integer getGold() {
        return gold;
    }

    public String getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(String paymentDetail) {
        this.paymentDetail = paymentDetail;
    }
}
