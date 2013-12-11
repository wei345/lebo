package com.lebo.repository.mybatis;

import com.lebo.entity.GoldOrder;
import com.lebo.repository.MyBatisRepository;

/**
 * @author: Wei Liu
 * Date: 13-10-31
 * Time: PM4:50
 */
@MyBatisRepository
public interface GoldOrderDao {
    GoldOrder get(Long id);

    void insert(GoldOrder goldOrder);

    void updateStatus(GoldOrder goldOrder);

    int countByAlipayNotifyId(String alipayNotifyId);
}
