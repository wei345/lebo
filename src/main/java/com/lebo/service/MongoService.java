package com.lebo.service;

import com.lebo.repository.MongoErrorCode;
import com.mongodb.MongoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author: Wei Liu
 * Date: 13-7-4
 * Time: PM9:01
 */
public abstract class MongoService {
    @Autowired
    protected MongoTemplate mongoTemplate;

    protected void checkMongoError() {
        try {
            mongoTemplate.executeCommand("{ getLastError : 1 }").throwOnError();
        } catch (MongoException e) {
            switch (e.getCode()) {
                case MongoErrorCode.DUPLICATE_KEY_ERROR:
                    throw new DuplicateException(e);
                default:
                    throw e;
            }
        }
    }

}
