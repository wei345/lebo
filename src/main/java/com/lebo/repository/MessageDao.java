package com.lebo.repository;

import com.lebo.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-7-14
 * Time: PM8:01
 */
public interface MessageDao extends MongoRepository<Message, String> {
}
