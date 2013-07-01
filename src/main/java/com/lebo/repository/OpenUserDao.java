package com.lebo.repository;

import com.lebo.entity.OpenUser;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-7-1
 * Time: PM12:12
 */
public interface OpenUserDao extends MongoRepository<OpenUser, String> {
    OpenUser findByProviderAndUid(String provider, String uid);
}
