package com.lebo.repository.mybatis;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

/**
 * @author: Wei Liu
 * Date: 13-10-31
 * Time: PM5:11
 */
public class ProductDaoTest extends SpringContextTestCase {
    @Autowired
    private ProductDao productDao;

    @Test
    public void get() throws Exception {
        Product product = productDao.get(1L);
        assertTrue(1 == product.getProductId());
    }
}
