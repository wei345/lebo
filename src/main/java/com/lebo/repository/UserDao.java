package com.lebo.repository;

import com.lebo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserDao extends MongoRepository<User, String> {

    @Query(value = "{}")
    Page<User> searchUser(String q, Pageable pageable);

    User findByScreenName(String screenName);

    User findByOAuthIds(String oAuthId);
}
