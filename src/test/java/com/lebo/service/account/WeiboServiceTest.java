package com.lebo.service.account;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Wei Liu
 * Date: 13-9-9
 * Time: AM10:18
 */
public class WeiboServiceTest extends SpringContextTestCase {
    @Autowired
    private WeiboService weiboService;

    @Test
    public void getUid(){
        String uid = weiboService.getUid("2.00TgJysBz7QwTC50dd2ad1facNOSYD");
        System.out.println(uid);
    }

    @Test
    public void getFriends() throws Exception {
        WeiboService.WeiboFriend weiboFriend = weiboService.getFriends("2.00TgJysBz7QwTC50dd2ad1facNOSYD", "1728391885", 10, 0);
        for(WeiboService.WeiboUser user : weiboFriend.getUsers()){
            System.out.println(user.getScreen_name());
        }
    }
}
