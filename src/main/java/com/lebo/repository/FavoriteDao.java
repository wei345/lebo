package com.lebo.repository;

import com.lebo.entity.Favorite;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


/**
 * @author: Wei Liu
 * Date: 13-7-12
 * Time: PM12:21
 */
public interface FavoriteDao extends MongoRepository<Favorite, String> {
    Favorite findByUserIdAndPostId(String userId, String postId);

    @Query(value = "{ userId : ?0 , postId : { $lt : ?1, $gt : ?2 } }")
    List<Favorite> findByUserId(String userId, String maxId, String sinceId, Pageable pageable);
}
