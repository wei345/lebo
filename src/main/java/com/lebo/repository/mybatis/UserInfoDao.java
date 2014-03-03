package com.lebo.repository.mybatis;

import com.lebo.entity.UserInfo;
import com.lebo.repository.MyBatisRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

    List<UserInfo> getOrderByConsumeGoldDesc(Pageable pageable);

    List<UserInfo> getOrderByPopularityDesc(Pageable pageable);
}
