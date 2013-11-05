package com.lebo.rest.dto;

import java.math.BigDecimal;

/**
 * @author: Wei Liu
 * Date: 13-11-4
 * Time: PM7:46
 */
public class UserGoodsDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String imageNormal;
    private String imageBigger;
    private Integer quantity;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
