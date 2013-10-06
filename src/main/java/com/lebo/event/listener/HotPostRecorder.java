package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.entity.HotPost;
import com.lebo.event.AfterPostDestroyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * 热门帖子.监听所有影响热门帖子的操作。
 *
 * @author: Wei Liu
 * Date: 13-10-6
 * Time: PM8:41
 */
@Component
public class HotPostRecorder {
    @Autowired
    protected MongoTemplate mongoTemplate;

    /**
     * 当帖子被删除时，也要从热门帖子中删除
     */
    @Subscribe
    public void deleteOnPostDestroy(AfterPostDestroyEvent event) {
        if (event.getPost().getOriginPostId() == null) {
            mongoTemplate.remove(new Query(new Criteria(HotPost.POST_ID_KEY).is(event.getPost().getId())), HotPost.class);
        }
    }
}
