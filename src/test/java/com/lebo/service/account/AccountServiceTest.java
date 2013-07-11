package com.lebo.service.account;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Wei Liu
 * Date: 13-7-12
 * Time: AM1:20
 */
public class AccountServiceTest extends SpringContextTestCase {
    @Autowired
    private AccountService accountService;

    @Test
    public void updateLastSignInAt() {
        User user = new User();
        user.setId("51dee0c71a882943dcd7160e");
        accountService.updateLastSignInAt(user);
    }
}
