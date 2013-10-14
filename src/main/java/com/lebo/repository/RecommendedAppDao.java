package com.lebo.repository;

import com.lebo.entity.RecommendedApp;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-10-14
 * Time: PM3:51
 */
public interface RecommendedAppDao extends MongoRepository<RecommendedApp, String> {
}
