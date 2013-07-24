package com.lebo.service.account;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author: Wei Liu
 * Date: 13-7-17
 * Time: PM4:59
 */
public class ShiroRenRenLoginTest extends SpringContextTestCase {
    @Autowired
    private ShiroRenRenLogin shiroRenRenLogin;

    @Test
    public void getUserInfo() {
        Map userInfo = shiroRenRenLogin.getUserInfo("232244|6.480f13a58a6da99eb659bbcfdd9b119d.2592000.1376625600-544808317");
        assertEquals("刘伟", ((Map) userInfo.get("response")).get("name"));
    }

    @Test
    public void newUserScreenName() {
        String screenName = shiroRenRenLogin.newScreenName("法图_麦");
        System.out.println(screenName);

        assertEquals(screenName, shiroRenRenLogin.newScreenName(screenName));
    }
}
