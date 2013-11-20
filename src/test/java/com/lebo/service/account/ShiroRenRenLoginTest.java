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
    public void newUserScreenName() {
        String screenName = shiroRenRenLogin.newScreenName("法图_麦");
        System.out.println(screenName);

        assertEquals(screenName, shiroRenRenLogin.newScreenName(screenName));
    }
}
