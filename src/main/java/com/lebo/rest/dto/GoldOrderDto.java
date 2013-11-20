package com.lebo.rest.dto;

import com.lebo.entity.GoldOrder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-11-4
 * Time: PM6:10
 */
public class GoldOrderDto {
    private Long id;
    private String userId;
    private GoldProductDto goldProduct;
    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal totalCost;
    private Date orderDate;
    private GoldOrder.Status status;
    private String alipayStatus;
    private String alipaySubject;
    private String alipayBody;

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

    public GoldProductDto getGoldProduct() {
        return goldProduct;
    }

    public void setGoldProduct(GoldProductDto goldProduct) {
        this.goldProduct = goldProduct;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public GoldOrder.Status getStatus() {
        return status;
    }

    public void setStatus(GoldOrder.Status status) {
        this.status = status;
    }

    public String getAlipayStatus() {
        return alipayStatus;
    }

    public void setAlipayStatus(String alipayStatus) {
        this.alipayStatus = alipayStatus;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getAlipaySubject() {
        return alipaySubject;
    }

    public void setAlipaySubject(String alipaySubject) {
        this.alipaySubject = alipaySubject;
    }

    public String getAlipayBody() {
        return alipayBody;
    }

    public void setAlipayBody(String alipayBody) {
        this.alipayBody = alipayBody;
    }
}
