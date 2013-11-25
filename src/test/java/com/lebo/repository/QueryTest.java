package com.lebo.repository;

import org.junit.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.Assert.assertEquals;

/**
 * @author: Wei Liu
 * Date: 13-11-25
 * Time: PM3:30
 */
public class QueryTest {

    @Test
    public void test() {
        Query query;

        query = new Query();
        query.addCriteria(new Criteria("a").is("aaa"));
        assertEquals("{ \"a\" : \"aaa\"}", query.getQueryObject().toString());

        query = new Query();
        Criteria c = new Criteria("a").is("aaa").ne("a");
        assertEquals("{ \"a\" : \"aaa\" , \"$ne\" : \"a\"}", c.getCriteriaObject().toString());
        assertEquals("{ \"a\" : \"aaa\" , \"$ne\" : \"a\"}", query.addCriteria(c).getQueryObject().toString());

        query.addCriteria(new Criteria("b").is("bbb").ne("b"));
        assertEquals("{ \"a\" : \"aaa\" , \"$ne\" : \"b\" , \"b\" : \"bbb\"}", query.getQueryObject().toString());

        c = new Criteria("a").is("aaa").and("b").is("bbb");
        assertEquals("b", c.getKey());
        assertEquals("{ \"a\" : \"aaa\" , \"b\" : \"bbb\"}", c.getCriteriaObject().toString());

        assertEquals("{ \"$or\" : [ { \"a\" : \"aaa\"} , { \"b\" : \"bbb\"}]}", new Criteria().orOperator(new Criteria[]{new Criteria("a").is("aaa"), new Criteria("b").is("bbb")}).getCriteriaObject().toString());
        assertEquals("{ \"$or\" : [ { \"a\" : \"aaa\"} , { \"b\" : \"bbb\"} , { \"b\" : \"bb\"}]}", new Criteria().orOperator(new Criteria[]{new Criteria("a").is("aaa"), new Criteria("b").is("bbb"), new Criteria("b").is("bb")}).getCriteriaObject().toString());

        c = new Criteria("a").is("aaa").orOperator(new Criteria[]{new Criteria("b").is("bbb")});
        assertEquals("{ \"a\" : \"aaa\" , \"$or\" : [ { \"b\" : \"bbb\"}]}", c.getCriteriaObject().toString());
    }
}
