package com.lebo.repository;

import com.lebo.entity.Option;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-7-18
 * Time: PM5:20
 */
public interface OptionDao extends MongoRepository<Option, String>{

}
