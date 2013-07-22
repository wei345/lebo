package com.lebo.repository;

import com.lebo.entity.Setting;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-7-18
 * Time: PM5:20
 */
public interface SettingDao extends MongoRepository<Setting, String>{

}
