package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.entity.User;
import com.lebo.event.AfterPostCreateEvent;
import com.lebo.event.AfterPostDestroyEvent;
import com.lebo.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * @author: Wei Liu
 * Date: 13-8-22
 * Time: PM6:40
 */
@Component
public class StatusCountRecorder {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private StatusService statusService;

    @Subscribe
    public void updateStatusesCount(AfterPostCreateEvent event) {
        if (event.getPost().isPublic()) {
            updateStatusesCount(event.getPost().getUserId());
        }
    }

    @Subscribe
    public void updateStatusesCount(AfterPostDestroyEvent event) {
        if (event.getPost().isPublic()) {
            updateStatusesCount(event.getPost().getUserId());
        }
    }

    private void updateStatusesCount(String userId) {
        int count = statusService.countUserPublicStatus(userId);

        mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(userId)),
                new Update().set(User.STATUSES_COUNT_KEY, count), User.class);
    }
}
