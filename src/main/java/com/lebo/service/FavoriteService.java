package com.lebo.service;

import com.lebo.entity.Favorite;
import com.lebo.entity.Post;
import com.lebo.event.AfterFavoriteCreateEvent;
import com.lebo.event.AfterFavoriteDestroyEvent;
import com.lebo.event.ApplicationEventBus;
import com.lebo.repository.FavoriteDao;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PaginationParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-12
 * Time: PM12:22
 */
@Service
public class FavoriteService extends AbstractMongoService {
    @Autowired
    private FavoriteDao favoriteDao;
    @Autowired
    private StatusService statusService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ApplicationEventBus eventBus;

    public void create(String userId, String postId) {
        Post post = statusService.getPost(postId);
        if (post == null) {
            throw new ServiceException(postId + "不存在");
        }

        if (StringUtils.isNotBlank(post.getOriginPostId())) {
            post = statusService.getPost(post.getOriginPostId());
            if (post == null) {
                throw new ServiceException("原始Post不存在");
            }
        }

        Favorite favorite = new Favorite(userId, post.getId(), post.getUserId());
        favoriteDao.save(favorite);
        throwOnMongoError();
        eventBus.post(new AfterFavoriteCreateEvent(favorite));
    }

    public void destroy(String userId, String postId) {
        Favorite favorite = favoriteDao.findByUserIdAndPostId(userId, postId);
        if (favorite != null) {
            favoriteDao.delete(favorite);
            throwOnMongoError();
            eventBus.post(new AfterFavoriteDestroyEvent(favorite));
        }
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

    public List<Post> list(String userId, PaginationParam paginationParam) {
        List<Favorite> favorites = favoriteDao.findByUserId(userId,
                paginationParam.getMaxId(), paginationParam.getSinceId(), paginationParam);
        List<String> ids = new ArrayList<String>();
        for (int i = 0; i < favorites.size(); i++) {
            ids.add(favorites.get(i).getPostId());
        }
        //TODO statusService.findPosts(ids)使用缓存，用ID一次查一个
        return statusService.findPosts(ids);
    }

    public void deleteByPostId(String postId) {
        mongoTemplate.remove(new Query(new Criteria(Favorite.POST_ID_KEY).is(postId)),
                Favorite.class);
    }
}
