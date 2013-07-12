package com.lebo.repository;

import com.lebo.entity.Favorite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-7-12
 * Time: PM12:21
 */
public interface FavoriteDao extends MongoRepository<Favorite, String> {
    Favorite findByUserIdAndPostId(String userId, String postId);
}
