package com.lebo.repository;

import com.lebo.entity.Post;
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
public interface PostDao extends MongoRepository<Post, String> {
    Page<Post> findByUserId(String userId, Pageable pageable);

    Post findByUserIdAndOriginPostId(String userId, String originPostId);

    @Query(value = "{ userId : ?0 , _id : { $lt : { $oid : ?1 }, $gt : { $oid : ?2 } } }")
    Page<Post> userTimeline(String userId, String maxId, String sinceId, Pageable pageable);

    @Query(value = "{ userId : { $in : ?0 } , _id : { $lt : { $oid : ?1 }, $gt : { $oid : ?2 } } }")
    Page<Post> homeTimeline(List<String> userId, String maxId, String sinceId, Pageable pageable);

    @Query(value = "{ userMentions : ?0 , _id : { $lt : { $oid : ?1 }, $gt : { $oid : ?2 } } }")
    Page<Post> mentionsTimeline(String userId, String maxId, String sinceId, Pageable pageable);

    @Query(value = "{ _id : { $in : ?0 } }")
    List<Post> findPosts(List<ObjectId> ids);

    @Query(value = "{ userId : ?0 , digested: true, _id : { $lt : { $oid : ?1 }, $gt : { $oid : ?2 } } }")
    Page<Post> usreDigestline(String userId, String maxId, String sinceId, Pageable pageable);
}
