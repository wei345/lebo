package com.lebo.entity;

/**
 * @author: Wei Liu
 * Date: 13-10-30
 * Time: PM3:37
 */
public class ProductCategory {
    private Long productCategoryId;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }
}
