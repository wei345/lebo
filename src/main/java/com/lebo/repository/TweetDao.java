package com.lebo.repository;

import com.lebo.entity.Tweet;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-7-3
 * Time: PM5:15
 */
public interface TweetDao extends MongoRepository<Tweet, String> {

}
