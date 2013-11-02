package com.lebo.repository.mybatis;

import com.lebo.entity.ProductCategory;
import com.lebo.repository.MyBatisRepository;

/**
 * @author: Wei Liu
 * Date: 13-11-2
 * Time: AM11:57
 */
@MyBatisRepository
public interface ProductCategoryDao {
    ProductCategory get(Long id);
}
