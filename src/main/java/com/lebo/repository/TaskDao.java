package com.lebo.repository;

import com.lebo.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-9-27
 * Time: PM4:53
 */
public interface TaskDao extends MongoRepository<Task, String> {
}
