package com.lebo.entity;

import java.math.BigDecimal;

/**
 * @author: Wei Liu
 * Date: 13-10-30
 * Time: PM5:00
 */
public class OrderDetail {
    private Product product;
    private Order order;
    private Integer quantity;
    private BigDecimal total;
    private BigDecimal discount;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
}
