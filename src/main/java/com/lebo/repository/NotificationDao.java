package com.lebo.repository;

import com.lebo.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-8-8
 * Time: PM7:08
 */
public interface NotificationDao extends MongoRepository<Notification, String> {
    @Query(value = "{ recipientId : ?0, _id : { $lt : { $oid : ?1 }, $gt : { $oid : ?2 } } }")
    List<Notification> find(String recipientId, String maxId, String sinceId, Pageable pageable);

    @Query(value = "{ recipientId : ?0, unread : true, _id : { $lt : { $oid : ?1 }, $gt : { $oid : ?2 } } }")
    List<Notification> findUnread(String recipientId, String maxId, String sinceId, Pageable pageable);
}
