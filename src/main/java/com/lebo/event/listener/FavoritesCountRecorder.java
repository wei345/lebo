package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.event.AfterCreateFavoriteEvent;
import com.lebo.event.AfterDestroyFavoriteEvent;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Wei Liu
 * Date: 13-7-29
 * Time: PM3:28
 */
@Component
public class FavoritesCountRecorder {
    @Autowired
    private AccountService accountService;
    @Autowired
    private StatusService statusService;

    @Subscribe
    public void increase(AfterCreateFavoriteEvent e) {
        accountService.increaseFavoritesCount(e.getFavorite().getUserId());
        statusService.increaseFavoritesCount(e.getFavorite().getPostId());
    }

    @Subscribe
    public void decrease(AfterDestroyFavoriteEvent e) {
        accountService.decreaseFavoritesCount(e.getFavorite().getUserId());
        statusService.decreaseFavoritesCount(e.getFavorite().getPostId());
    }
}
