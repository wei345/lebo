package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.entity.Post;
import com.lebo.event.AfterFavoriteCreateEvent;
import com.lebo.event.AfterFavoriteDestroyEvent;
import com.lebo.event.AfterPostCreateEvent;
import com.lebo.event.AfterPostDestroyEvent;
import com.lebo.service.HashtagService;
import com.lebo.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Wei Liu
 * Date: 13-7-31
 * Time: PM4:05
 */
@Component
public class HashtagRecorder {

    @Autowired
    private HashtagService hashtagService;
    @Autowired
    private StatusService statusService;

    /**
     * post数增长。
     *
     * @param event
     */
    @Subscribe
    public void saveHashtags(AfterPostCreateEvent event) {
        for (String name : event.getPost().getHashtags()) {
            hashtagService.increasePostsCount(name);
        }
    }

    /**
     * post数减少。
     *
     * @param event
     */
    @Subscribe
    public void removeHashtags(AfterPostDestroyEvent event) {
        for (String name : event.getPost().getHashtags()) {
            hashtagService.decreasePostsCount(name);
        }
    }

    /**
     * 红心(收藏)数增长。
     *
     * @param e
     */
    @Subscribe
    public void increaseFavoritesCount(AfterFavoriteCreateEvent e) {
        Post post = statusService.getPost(e.getFavorite().getPostId());
        for (String name : post.getHashtags()) {
            hashtagService.increaseFavoritesCount(name);
        }
    }

    /**
     * 红心(收藏)数减少。
     *
     * @param e
     */
    @Subscribe
    public void decreaseFavoritesCount(AfterFavoriteDestroyEvent e) {
        Post post = statusService.getPost(e.getFavorite().getPostId());
        for (String name : post.getHashtags()) {
            hashtagService.decreaseFavoritesCount(name);
        }
    }

    //TODO 记录viewCount

}
