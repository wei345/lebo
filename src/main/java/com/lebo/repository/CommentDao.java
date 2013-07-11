package com.lebo.repository;

import com.lebo.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-7-11
 * Time: AM8:22
 */
public interface CommentDao extends MongoRepository<Comment, String> {
}
