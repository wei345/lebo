package com.lebo.repository;

import com.lebo.entity.Statistics;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-10-25
 * Time: PM3:38
 */
public interface StatisticsDao extends MongoRepository<Statistics, String> {

}
