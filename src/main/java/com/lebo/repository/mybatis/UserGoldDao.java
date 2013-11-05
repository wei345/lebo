package com.lebo.repository.mybatis;

import com.lebo.entity.UserGold;
import com.lebo.repository.MyBatisRepository;

/**
 * @author: Wei Liu
 * Date: 13-11-4
 * Time: PM4:45
 */
@MyBatisRepository
public interface UserGoldDao {
    Long getUserGoldQuantity(String userId);

    UserGold getByUserId(String userId);

    void updateUserGoldQuantity(UserGold userGold);

    void insert(UserGold userGold);
}
