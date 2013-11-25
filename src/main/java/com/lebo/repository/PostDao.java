package com.lebo.repository;

import com.lebo.entity.Post;
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
    Post findByUserIdAndOriginPostId(String userId, String originPostId);
}
