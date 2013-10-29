package com.lebo.repository;

import com.lebo.entity.Im;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM1:16
 */
public interface ImDao extends MongoRepository<Im, String> {
}
