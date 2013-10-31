package com.lebo.repository.mybatis;

import com.lebo.entity.OrderDetail;
import com.lebo.repository.MyBatisRepository;

/**
 * @author: Wei Liu
 * Date: 13-10-30
 * Time: PM7:13
 */
@MyBatisRepository
public interface OrderDetailDao {
    void save(OrderDetail orderDetail);
}
