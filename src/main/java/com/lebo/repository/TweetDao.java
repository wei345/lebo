package com.lebo.repository;

import com.lebo.entity.Tweet;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-3
 * Time: PM5:15
 */
public interface TweetDao extends MongoRepository<Tweet, String> {
    Page<Tweet> findByUserId(String userId, Pageable pageable);

    @Query(value = "{ userId : ?0 , _id : { $lte : { $oid : ?1 }, $gt : { $oid : ?2 } } }")
    Page<Tweet> userTimeline(String userId, String maxId, String sinceId, Pageable pageable);
}
