package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.event.AfterFollowingCreatEvent;
import com.lebo.event.AfterFollowingDestroyEvent;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Wei Liu
 * Date: 13-7-29
 * Time: PM2:01
 */
@Component
public class FriendsCountRecorder {
    @Autowired
    private AccountService accountService;


    @Subscribe
    public void increase(AfterFollowingCreatEvent e) {
        accountService.updateFollowersCount(e.getFollowingId());
        accountService.updateFriendsCount(e.getUserId());
        //TODO 维护双向关注数量(还未用到2013.8.14)
    }

    @Subscribe
    public void decrease(AfterFollowingDestroyEvent e) {
        accountService.updateFollowersCount(e.getFollowingId());
        accountService.updateFriendsCount(e.getUserId());
    }
}
