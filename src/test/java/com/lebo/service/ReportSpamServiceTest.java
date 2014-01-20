package com.lebo.service;

import org.junit.Test;
import org.springframework.data.mongodb.core.query.Criteria;

import static org.junit.Assert.assertEquals;

/**
 * @author: Wei Liu
 * Date: 14-1-16
 * Time: PM6:33
 */
public class ReportSpamServiceTest {
    @Test
    public void isReported() {

        Criteria criteria = new Criteria("a").is("1");

        assertEquals("{ \"a\" : \"1\"}", criteria.getCriteriaObject().toString());

        criteria.and("b").is(2).and("c").is(3);

        assertEquals("{ \"a\" : \"1\" , \"b\" : 2 , \"c\" : 3}", criteria.getCriteriaObject().toString());
    }
}
