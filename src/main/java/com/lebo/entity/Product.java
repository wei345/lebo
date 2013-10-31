package com.lebo.entity;

import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * @author: Wei Liu
 * Date: 13-10-30
 * Time: PM3:22
 */
public class Product {
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private PriceUnit priceUnit;
    private BigDecimal discount;
    private String image;
    private ProductCategory productCategory;

    public static enum PriceUnit {
        GOLD, //金币
        RMB
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public PriceUnit getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(PriceUnit priceUnit) {
        this.priceUnit = priceUnit;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * 应付金额
     *
     * @return price * ( 1 - discount )
     */
    public BigDecimal getCost() {
        Assert.notNull(price);
        Assert.notNull(discount);
        return price.multiply(BigDecimal.ONE.subtract(discount));
    }
}
