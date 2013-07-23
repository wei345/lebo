package com.lebo.repository;

import com.lebo.entity.Block;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-7-23
 * Time: PM3:55
 */
public interface BlockDao extends MongoRepository<Block, String> {
    Block findByUserIdAndBlockedId(String userId, String blockedId);

}
