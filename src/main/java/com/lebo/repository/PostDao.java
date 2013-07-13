package com.lebo.repository;

import com.lebo.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author: Wei Liu
 * Date: 13-7-3
 * Time: PM5:15
 */
public interface PostDao extends MongoRepository<Post, String> {
    Page<Post> findByUserId(String userId, Pageable pageable);

    @Query(value = "{ userId : ?0 , _id : { $lt : { $oid : ?1 }, $gt : { $oid : ?2 } } }")
    Page<Post> userTimeline(String userId, String maxId, String sinceId, Pageable pageable);

    @Query(value = "{ userId : { $in : ?0 } , _id : { $lt : { $oid : ?1 }, $gt : { $oid : ?2 } } }")
    Page<Post> homeTimeline(List<String> userId, String maxId, String sinceId, Pageable pageable);

    @Query(value = "{ mentions : ?0 , _id : { $lt : { $oid : ?1 }, $gt : { $oid : ?2 } } }")
    Page<Post> mentionsTimeline(String userId, String maxId, String sinceId, Pageable pageable);

    @Query(value = "{ text : { $regex : ?0, $options: 'i' } , _id : { $lt : { $oid : ?1 }, $gt : { $oid : ?2 } } }")
    Page<Post> searchText(String q, String maxId, String sinceId, Pageable pageable);

    @Query(value = "{ tags : { $regex : ?0, $options: 'i' } , _id : { $lt : { $oid : ?1 }, $gt : { $oid : ?2 } } }")
    Page<Post> searchTags(String q, String maxId, String sinceId, Pageable pageable);
}
