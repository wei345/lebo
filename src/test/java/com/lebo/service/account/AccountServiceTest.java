package com.lebo.service.account;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void increaseFollowersCount() {
        accountService.increaseFollowersCount("51def1ce1a883914869e46f2");
    }

    @Test
    public void isScreenNameAvailable() {
        assertFalse(accountService.isScreenNameAvailable("家有笨猫咪", null));
        //本人可用
        assertTrue(accountService.isScreenNameAvailable("家有笨猫咪", "51def53f1a883914869e46f5"));
        //非本人不可用
        assertFalse(accountService.isScreenNameAvailable("家有笨猫咪", "51dfd3d21a8855744379891f"));
    }

    @Test
    public void isMongoId() {
        assertTrue(accountService.isMongoId("51dfd3d21a8855744379891f"));
        assertTrue(accountService.isMongoId("51DFD3D21A8855744379891F"));
        assertFalse(accountService.isMongoId("51DFD3D21A8855744379891F."));
    }

    @Test
    public void isScreenNameValid() {
        //合法字符
        assertTrue(accountService.isScreenNameValid("中文_-abc0A"));
        //不能少于4个字符
        assertFalse(accountService.isScreenNameValid("中文_"));
        //不能多于24个字符
        assertFalse(accountService.isScreenNameValid("0123456789012345678901234"));
        //不能带有Emoji
        assertFalse(accountService.isScreenNameValid("中文_-abc0A\uD83D\uDC94"));

        //.能匹配Emoji
        assertTrue("\uD83D\uDC94".matches(".+"));
        //中文不匹配Emoji
        assertFalse("\uD83D\uDC94".matches("[\u4e00-\u9fa5]+"));
    }

    @Test
    public void refreshFastestRisingUsers(){
        accountService.refreshFastestRisingUsers();
    }
}
