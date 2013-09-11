package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.entity.User;
import com.lebo.event.AfterPostDestroyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * @author: Wei Liu
 * Date: 13-9-11
 * Time: PM7:05
 */
@Component
public class ViewCountRecoder {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Subscribe
    public void decreaseViewCount(AfterPostDestroyEvent event) {
        //删除原始帖时，更新作者视频播放数
        if (event.getPost().getOriginPostId() == null
                && event.getPost().getViewCount() != null
                && event.getPost().getViewCount() > 0) {

            mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(event.getPost().getUserId())),
                    new Update().inc(User.VIEW_COUNT_KEY, event.getPost().getViewCount() * -1),
                    User.class);
        }
    }
}
