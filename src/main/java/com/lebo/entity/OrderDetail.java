package com.lebo.entity;

import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * @author: Wei Liu
 * Date: 13-10-30
 * Time: PM5:00
 */
public class OrderDetail {
    private Order order;
    private Product product;
    private Integer quantity;
    private BigDecimal discount;

    public OrderDetail() {
    }

    public OrderDetail(Order order, Product product, Integer quantity, BigDecimal discount) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.discount = discount;
    }

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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * 总价格
     *
     * @return product.price * quantity
     */
    public BigDecimal getTotalPrice() {
        Assert.notNull(product);
        Assert.notNull(quantity);
        return product.getPrice().multiply(new BigDecimal(quantity));
    }

    /**
     * 应付金额
     *
     * @return product.cost * quantity * ( 1 - discount )
     */
    public BigDecimal getTotalCost() {
        Assert.notNull(discount);
        Assert.notNull(product);
        return product.getCost()
                .multiply(new BigDecimal(quantity))
                .multiply(BigDecimal.ONE.subtract(discount));
    }
}
