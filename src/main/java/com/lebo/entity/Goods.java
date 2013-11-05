package com.lebo.entity;

import java.math.BigDecimal;

/**
 * @author: Wei Liu
 * Date: 13-11-4
 * Time: PM7:18
 */
public class Goods {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private BigDecimal discount;
    private String imageNormal;
    private String imageBigger;

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getImageNormal() {
        return imageNormal;
    }

    public void setImageNormal(String imageNormal) {
        this.imageNormal = imageNormal;
    }

    public String getImageBigger() {
        return imageBigger;
    }

    public void setImageBigger(String imageBigger) {
        this.imageBigger = imageBigger;
    }
}
