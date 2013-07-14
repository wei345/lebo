package com.lebo.repository;

import com.lebo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserDao extends MongoRepository<User, String> {

    @Query(value = "{ screenName : { $regex : ?0, $options: 'i' } , _id : { $lt : { $oid : ?1 }, $gt : { $oid : ?2 } } }")
    Page<User> search(String q, String maxId, String minId, Pageable pageable);

    User findByScreenName(String screenName);

    User findByOAuthIds(String oAuthId);
}
