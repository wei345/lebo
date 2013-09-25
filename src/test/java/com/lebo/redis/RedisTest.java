package com.lebo.redis;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.nosql.redis.JedisTemplate;
import static org.junit.Assert.*;

/**
 * @author: Wei Liu
 * Date: 13-9-25
 * Time: PM5:13
 */
public class RedisTest extends SpringContextTestCase {
    @Autowired
    private JedisTemplate jedisTemplate;

    @Test
    public void test(){
        jedisTemplate.set("foo", "bar");
        assertEquals("bar", jedisTemplate.get("foo"));
    }
}
