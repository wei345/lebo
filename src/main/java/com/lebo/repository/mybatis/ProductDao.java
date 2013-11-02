package com.lebo.repository.mybatis;

import com.lebo.entity.Product;
import com.lebo.repository.MyBatisRepository;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-10-31
 * Time: PM4:50
 */
@MyBatisRepository
public interface ProductDao {
    Product get(Long id);

    Product getWithDetail(Long id);

    List<Product> findByCategoryId(Long categoryId);
}
