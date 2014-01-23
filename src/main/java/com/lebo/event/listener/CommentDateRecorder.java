package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.entity.Post;
import com.lebo.event.AfterCommentCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * 记录帖子的最后评论时间.
 *
 * @author: Wei Liu
 * Date: 13-12-18
 * Time: PM6:54
 */
@Component
public class CommentDateRecorder {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Subscribe
    public void updatePostLastCommentDate(AfterCommentCreateEvent event) {

        mongoTemplate.updateFirst(
                new Query(new Criteria(Post.ID_KEY).is(event.getComment().getPostId())),
                new Update().set(Post.LAST_COMMENT_CREATED_AT_KEY, event.getComment().getCreatedAt()),
                Post.class);
    }
}
