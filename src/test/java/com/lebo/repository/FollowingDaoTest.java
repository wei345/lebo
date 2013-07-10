package com.lebo.repository;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.Following;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author: Wei Liu
 * Date: 13-7-10
 * Time: PM1:40
 */
public class FollowingDaoTest extends SpringContextTestCase {
    @Autowired
    private FollowingDao followingDao;

    @Test
    public void findByUserId() {
        List<Following> list = followingDao.findByUserId("51dcf1d81a883e712783f124");
        assertEquals(2, list.size());
    }
}
