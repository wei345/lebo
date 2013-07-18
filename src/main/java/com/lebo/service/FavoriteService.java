package com.lebo.service;

import com.lebo.entity.Comment;
import com.lebo.entity.Favorite;
import com.lebo.entity.Post;
import com.lebo.repository.FavoriteDao;
import com.lebo.rest.dto.StatusDto;
import com.lebo.service.param.PaginationParam;
import com.lebo.service.param.ShowCommentParam;
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

    public void create(String userId, String postId) {
        favoriteDao.save(new Favorite(userId, postId));
    }

    public void destroy(String userId, String postId){
        Favorite favorite = favoriteDao.findByUserIdAndPostId(userId, postId);
        favoriteDao.delete(favorite);
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

    public List<StatusDto> show(String userId, PaginationParam param){
        List<Favorite> favorites = favoriteDao.findByUserId(userId, param);
        List<String> ids = new ArrayList<String>();
        for(int i=0; i<favorites.size(); i++){
            ids.add(favorites.get(i).getPostId());
        }
        // 获取Posts
        List<Post> posts = statusService.findPosts(ids);
        // posts转为StatusDto
        List<StatusDto> statusDtos = new ArrayList<StatusDto>();
        for(int i=0; i<posts.size(); i++){
            statusDtos.add(statusService.toStatusDto(posts.get(i)));
        }
        return statusDtos;
    }
}
