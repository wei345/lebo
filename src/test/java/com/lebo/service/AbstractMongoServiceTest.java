package com.lebo.service;

import com.lebo.SpringContextTestCase;
import com.lebo.service.param.PaginationParam;
import com.mongodb.DBObject;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void testSdf() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/HHmm");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 8, 10, 11, 57, 1);
        assertEquals("2013-09-10/1157", sdf.format(calendar.getTime()));

        calendar.set(2013, 8, 10, 1, 2, 3);
        assertEquals("2013-09-10/0102", sdf.format(calendar.getTime()));
    }
}
