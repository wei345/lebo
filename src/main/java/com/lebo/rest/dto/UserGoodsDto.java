package com.lebo.rest.dto;

/**
 * @author: Wei Liu
 * Date: 13-11-4
 * Time: PM7:46
 */
public class UserGoodsDto extends GoodsDto {
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
