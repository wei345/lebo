package com.lebo.repository;

import com.lebo.entity.Hashtag;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-7-31
 * Time: PM4:01
 */
public interface HashtagDao extends MongoRepository<Hashtag, String> {
}
