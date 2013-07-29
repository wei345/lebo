package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.event.AfterCreatFollowingEvent;
import com.lebo.event.AfterDestroyFollowingEvent;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Wei Liu
 * Date: 13-7-29
 * Time: PM2:01
 */
@Component
public class FollowerCountRecorder {
    @Autowired
    private AccountService accountService;


    @Subscribe
    public void increase(AfterCreatFollowingEvent e) {
        accountService.increaseFollowersCount(e.getFollowingId());
        //TODO 维护双向关注数量
    }

    @Subscribe
    public void decrease(AfterDestroyFollowingEvent e) {
        accountService.decreaseFollowersCount(e.getFollowingId());
    }
}
