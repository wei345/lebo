package com.lebo.rest.dto;

import com.lebo.entity.GoldProduct;

import java.math.BigDecimal;

/**
 * @author: Wei Liu
 * Date: 13-11-1
 * Time: PM5:30
 */
public class GoldProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private GoldProduct.PriceUnit priceUnit;
    private BigDecimal discount;
    private BigDecimal cost;
    private String imageUrl;
    private String inAppPurchaseId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public GoldProduct.PriceUnit getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(GoldProduct.PriceUnit priceUnit) {
        this.priceUnit = priceUnit;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getInAppPurchaseId() {
        return inAppPurchaseId;
    }

    public void setInAppPurchaseId(String inAppPurchaseId) {
        this.inAppPurchaseId = inAppPurchaseId;
    }
}
