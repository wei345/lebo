package com.lebo.repository;

import com.lebo.entity.ReportSpam;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 14-1-15
 * Time: PM3:38
 */
public interface ReportSpamDao extends MongoRepository<ReportSpam, String> {
}
