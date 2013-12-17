package com.lebo.repository.mybatis;

import com.lebo.entity.UserInfo;
import com.lebo.repository.MyBatisRepository;

/**
 * @author: Wei Liu
 * Date: 13-11-4
 * Time: PM4:45
 */
@MyBatisRepository
public interface UserInfoDao {
    UserInfo get(String userId);

    void update(UserInfo userInfo);

    void insert(UserInfo userInfo);
}
