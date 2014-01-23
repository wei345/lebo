package com.lebo.rest.dto;

/**
 * @author: Wei Liu
 * Date: 14-1-13
 * Time: PM10:09
 */
public class GoldProductWithQuantityDto extends GoldProductDto {
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
