package com.lebo.service;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: PM8:16
 */
public class FriendshipServiceTest extends SpringContextTestCase {
    @Autowired
    private FriendshipService friendshipService;

    @Test
    public void unfollow() {
        friendshipService.unfollow("51da5bd71a881f1acdcb6308", "51dbb3e21a887f15c8b6f042");
    }

    @Test
    public void countFollowers(){
        int count = friendshipService.countFollowers("51def53f1a883914869e46f5");
        System.out.println(count);
    }
}
