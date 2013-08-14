package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.event.AfterUserLoginEvent;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Wei Liu
 * Date: 13-8-16
 * Time: AM10:17
 */
@Component
public class LastLoginTimeListener {
    @Autowired
    private AccountService accountService;

    @Subscribe
    public void updateLastLoginTime(AfterUserLoginEvent event) {
        accountService.updateLastSignInAt(event.getUser());
    }
}
