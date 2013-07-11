package com.lebo.service;

import com.lebo.repository.MongoConstant;
import com.mongodb.MongoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author: Wei Liu
 * Date: 13-7-4
 * Time: PM9:01
 */
public abstract class AbstractMongoService {
    @Autowired
    protected MongoTemplate mongoTemplate;

    /**
     * 执行MongoDB的<code>getLastError</code>命令，如果发现错误状态则抛出异常。
     *
     * @throws DuplicateException 当违反唯一约束时
     * @throws MongoException     当其他错误状态时
     * @see com.mongodb.CommandResult#throwOnError()
     */
    protected void throwOnMongoError() {
        try {
            mongoTemplate.executeCommand("{ getLastError : 1 }").throwOnError();
        } catch (MongoException e) {
            switch (e.getCode()) {
                case MongoConstant.MONGO_ERROR_CODE_DUPLICATE_KEY:
                    throw new DuplicateException(e);
                default:
                    throw e;
            }
        }
    }

}