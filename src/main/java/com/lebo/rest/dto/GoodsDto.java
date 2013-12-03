package com.lebo.rest.dto;

/**
 * @author: Wei Liu
 * Date: 13-11-4
 * Time: PM7:17
 */
public class GoodsDto {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private String imageUrl;
    private String imageBiggerUrl;
    private String quantityUnit;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageBiggerUrl() {
        return imageBiggerUrl;
    }

    public void setImageBiggerUrl(String imageBiggerUrl) {
        this.imageBiggerUrl = imageBiggerUrl;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }
}
