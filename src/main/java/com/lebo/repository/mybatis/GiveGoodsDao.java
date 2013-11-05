package com.lebo.repository.mybatis;

import com.lebo.entity.GiveGoods;
import com.lebo.repository.MyBatisRepository;

/**
 * @author: Wei Liu
 * Date: 13-11-5
 * Time: PM2:58
 */
@MyBatisRepository
public interface GiveGoodsDao {
    void insert(GiveGoods giveGoods);
}
