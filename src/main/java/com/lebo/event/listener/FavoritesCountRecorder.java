package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.entity.Post;
import com.lebo.entity.User;
import com.lebo.event.AfterFavoriteCreateEvent;
import com.lebo.event.AfterFavoriteDestroyEvent;
import com.lebo.event.AfterPostDestroyEvent;
import com.lebo.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * @author: Wei Liu
 * Date: 13-7-29
 * Time: PM3:28
 */
@Component
public class FavoritesCountRecorder {
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Subscribe
    public void increase(AfterFavoriteCreateEvent e) {
        updateUserBeFavoritesCount(e.getFavorite().getPostUserId());
        updatePostFavoritesCount(e.getFavorite().getPostId());
    }

    @Subscribe
    public void decrease(AfterFavoriteDestroyEvent e) {
        updateUserBeFavoritesCount(e.getFavorite().getPostUserId());
        updatePostFavoritesCount(e.getFavorite().getPostId());
    }

    @Subscribe
    public void decreaseOnPostDestroy(AfterPostDestroyEvent event) {
        if (event.getPost().getFavoritesCount() > 0) {
            updateUserBeFavoritesCount(event.getPost().getUserId());
        }
    }

    private void updateUserBeFavoritesCount(String userId) {
        int count = favoriteService.countUserBeFavorites(userId);
        mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(userId)),
                new Update().set(User.BE_FAVORITED_COUNT_KEY, count),
                User.class);
    }

    private void updatePostFavoritesCount(String postId) {
        int favoritesCount = favoriteService.countPostFavorites(postId);

        Query query = new Query(new Criteria(Post.ID_KEY).is(postId));
        query.fields().include(Post.POPULARITY_KEY);

        Integer popularity = mongoTemplate.findOne(query, Post.class).getPopularity();
        if(popularity == null){
            popularity = 0;
        }

        mongoTemplate.updateFirst(new Query(new Criteria(Post.ID_KEY).is(postId)),
                new Update()
                        .set(Post.FAVOURITES_COUNT_KEY, favoritesCount)
                        .set(Post.FAVORITES_COUNT_ADD_POPULARITY_KEY, favoritesCount + popularity),
                Post.class);
    }
}
