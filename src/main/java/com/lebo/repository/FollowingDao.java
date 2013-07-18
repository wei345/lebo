package com.lebo.repository;

import com.lebo.entity.Following;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: PM4:56
 */
public interface FollowingDao extends MongoRepository<Following, String> {

    Following findByUserIdAndFollowingId(String userId, String followingId);
    List<Following> findByUserId(String userId);

    List<Following> findByUserId(String userId, Pageable pageable);
    List<Following> findByFollowingId(String followingId, Pageable pageable);
}
