package com.lebo.rest.dto;

import java.math.BigDecimal;

/**
 * @author: Wei Liu
 * Date: 13-11-1
 * Time: PM5:29
 */
public class OrderDetailDto {
    private ProductDto product;
    private Integer quantity;
    private BigDecimal discount;

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
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
}
