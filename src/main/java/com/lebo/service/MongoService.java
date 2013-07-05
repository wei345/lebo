package com.lebo.service;

import com.mongodb.CommandResult;
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

    protected void checkMongoError(){
        CommandResult commandResult = mongoTemplate.executeCommand("{ getLastError : 1 }");
        Object code = commandResult.get("code");
        if (code != null) {
            throw new RuntimeException(commandResult.get("err").toString());
        }
    }

}
