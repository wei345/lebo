package com.lebo.repository.mybatis;

import com.lebo.entity.Goods;
import com.lebo.repository.MyBatisRepository;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-11-5
 * Time: AM11:26
 */
@MyBatisRepository
public interface GoodsDao {
    Goods getById(Long id);
    List<Goods> getAll();
}
