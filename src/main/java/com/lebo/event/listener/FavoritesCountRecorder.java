package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.event.AfterFavoriteCreateEvent;
import com.lebo.event.AfterFavoriteDestroyEvent;
import com.lebo.event.AfterPostDestroyEvent;
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
    public void increase(AfterFavoriteCreateEvent e) {
        accountService.increaseFavoritesCount(e.getFavorite().getPostUserId());
        statusService.increaseFavoritesCount(e.getFavorite().getPostId());
    }

    @Subscribe
    public void decrease(AfterFavoriteDestroyEvent e) {
        accountService.decreaseFavoritesCount(e.getFavorite().getPostUserId());
        statusService.decreaseFavoritesCount(e.getFavorite().getPostId());
    }

    @Subscribe
    public void decreaseOnPostDestroy(AfterPostDestroyEvent event) {
        if (event.getPost().getFavoritesCount() > 0) {
            accountService.decreaseFavoritesCount(event.getPost().getUserId(), event.getPost().getFavoritesCount());
        }
    }
}
