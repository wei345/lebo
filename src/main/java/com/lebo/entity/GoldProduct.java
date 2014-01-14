package com.lebo.entity;

import com.lebo.service.FileContentUrlUtils;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * @author: Wei Liu
 * Date: 13-10-30
 * Time: PM3:22
 */
public class GoldProduct {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private PriceUnit priceUnit;
    private BigDecimal discount;
    private String image;
    private Integer gold;

    public static enum PriceUnit {
        CNY
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public String getImageUrl() {
        if (image == null) {
            return null;
        }

        return FileContentUrlUtils.getContentUrl(image);
    }

    /**
     * 应付金额
     *
     * @return price + discount
     */
    public BigDecimal getCost() {
        Assert.notNull(price);
        if (discount != null) {
            return price.add(discount);
        }
        return price;
    }

    public String getInAppPurchaseId() {
        return "gold_product_" + id;
    }
}
