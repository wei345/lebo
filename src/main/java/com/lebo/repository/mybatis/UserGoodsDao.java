package com.lebo.repository.mybatis;

import com.lebo.entity.UserGoods;
import com.lebo.repository.MyBatisRepository;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-11-5
 * Time: AM10:59
 */
@MyBatisRepository
public interface UserGoodsDao {
    List<UserGoods> getByUserId(String userId);

    UserGoods get(UserGoods userGoods);

    void insert(UserGoods userGoods);

    void updateQuantityByUserIdAndGoodsId(UserGoods userGoods);
}
