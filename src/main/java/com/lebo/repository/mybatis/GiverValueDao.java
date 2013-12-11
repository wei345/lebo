package com.lebo.repository.mybatis;

import com.lebo.entity.GiverValue;
import com.lebo.repository.MyBatisRepository;

import java.util.List;
import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 13-12-4
 * Time: PM6:41
 */
@MyBatisRepository
public interface GiverValueDao {

    GiverValue get(GiverValue giverValue);

    void update(GiverValue giverValue);

    void insert (GiverValue giverValue);

    List<GiverValue> getByUserIdOrderByGiveValueDesc(Map params);

    int countBefore(GiverValue giverValue);
}
