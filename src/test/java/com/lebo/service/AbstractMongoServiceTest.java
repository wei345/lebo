package com.lebo.service;

import com.lebo.SpringContextTestCase;
import com.lebo.service.param.PaginationParam;
import com.mongodb.DBObject;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author: Wei Liu
 * Date: 13-7-31
 * Time: PM1:51
 */
public class AbstractMongoServiceTest extends SpringContextTestCase {

    @Test
    public void pagination() {
        Query query = new Query();
        PaginationParam p = new PaginationParam();
        p.setMaxId("aaaaabbbbbcccccdddddeeee");
        p.setSinceId("aaaaabbbbbcccccdddddeeea");

        AbstractMongoService.paginationById(query, p);

        DBObject o = query.getQueryObject();
        System.out.println(o);
    }
}
