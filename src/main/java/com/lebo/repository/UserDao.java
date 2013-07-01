package com.lebo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.lebo.entity.User;
import org.springframework.stereotype.Repository;

public interface UserDao extends MongoRepository<User, String> {


}
