package com.lebo.entity;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.Assert.assertEquals;

/**
 * @author: Wei Liu
 * Date: 13-8-14
 * Time: PM11:04
 */
public class IdEntityTest extends SpringContextTestCase {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void id() {
        User user = mongoTemplate.findOne(new Query(new Criteria(IdEntity.ID_KEY).is("51e778ea1a8816dc79e40aaf")), User.class);
        assertEquals("liuwei", user.getScreenName());
    }
}
