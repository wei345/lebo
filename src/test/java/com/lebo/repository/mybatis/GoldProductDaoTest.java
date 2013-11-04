package com.lebo.repository.mybatis;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.GoldProduct;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author: Wei Liu
 * Date: 13-10-31
 * Time: PM5:11
 */
public class GoldProductDaoTest extends SpringContextTestCase {
    @Autowired
    private GoldProductDao goldProductDao;

    @Test
    public void get() throws Exception {
        GoldProduct goldProduct = goldProductDao.get(1L);
        assertTrue(1 == goldProduct.getId());
    }

}
