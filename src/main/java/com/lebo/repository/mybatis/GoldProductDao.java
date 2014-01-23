package com.lebo.repository.mybatis;

import com.lebo.entity.GoldProduct;
import com.lebo.repository.MyBatisRepository;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-10-31
 * Time: PM4:50
 */
@MyBatisRepository
public interface GoldProductDao {
    GoldProduct get(Long id);

    List<GoldProduct> getAll();

    List<GoldProduct> getActive();
}
