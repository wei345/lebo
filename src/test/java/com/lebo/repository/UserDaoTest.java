package com.lebo.repository;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.test.category.UnStable;

import static org.junit.Assert.assertEquals;

/**
 * @author: Wei Liu
 * Date: 13-6-24
 * Time: PM4:17
 */
public class UserDaoTest extends SpringContextTestCase implements UnStable {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    UserDao userDao;

    @Test
    public void get() {
        String id = "51c809301a884410d5ce6964";
        User user = userDao.findOne(id);
        assertEquals(id, user.getId());
    }
}
