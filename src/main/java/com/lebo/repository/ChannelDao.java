package com.lebo.repository;

import com.lebo.entity.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-9-10
 * Time: PM2:39
 */
public interface ChannelDao extends MongoRepository<Channel, String> {
}
