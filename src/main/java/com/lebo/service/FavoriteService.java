package com.lebo.service;

import com.lebo.entity.Favorite;
import com.lebo.repository.FavoriteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @author: Wei Liu
 * Date: 13-7-12
 * Time: PM12:22
 */
@Service
public class FavoriteService extends AbstractMongoService {
    @Autowired
    private FavoriteDao favoriteDao;

    public void create(String userId, String postId) {
        favoriteDao.save(new Favorite(userId, postId));
    }

    public boolean isFavorited(String userId, String postId) {
        return favoriteDao.findByUserIdAndPostId(userId, postId) != null;
    }

    public int countUserFavorites(String userId) {
        return (int) mongoTemplate.count(new Query(new Criteria(Favorite.USER_ID_KEY).is(userId)), Favorite.class);
    }

    public int countPostFavorites(String postId) {
        return (int) mongoTemplate.count(new Query(new Criteria(Favorite.POST_ID_KEY).is(postId)), Favorite.class);
    }
}
