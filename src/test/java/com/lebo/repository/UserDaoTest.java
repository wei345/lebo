package com.lebo.repository;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.test.category.UnStable;

import static com.mongodb.util.MyAsserts.assertFalse;
import static com.mongodb.util.MyAsserts.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    @Test
    public void findByScreenName() {
        User user = userDao.findByScreenName("法图_麦");
        assertNotNull(user);
        assertEquals("法图_麦", user.getScreenName());
    }

    @Test
    public void exists() {
        assertTrue(userDao.exists("51dbbc141a887f15c8b6f04c"));
        assertTrue(userDao.exists("51dbb3e21a887f15c8b6f042"));
        assertFalse(userDao.exists("123456789012345678901234"));
    }

    //@Test
    /*public void search() {
        SearchParam param = new SearchParam();
        List<User> users = userDao.search("麦", param.getMaxId(), param.getSinceId(), param).getContentUrl();

        assertTrue(users.size() > 0);
        assertTrue(users.get(0).getScreenName().contains("麦"));
    }*/
}
