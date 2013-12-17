package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.entity.Post;
import com.lebo.entity.User;
import com.lebo.event.AfterCommentCreateEvent;
import com.lebo.event.AfterCommentDestroyEvent;
import com.lebo.event.AfterPostCreateEvent;
import com.lebo.event.AfterPostDestroyEvent;
import com.lebo.service.CommentService;
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
    @Autowired
    private CommentService commentService;

    @Subscribe
    public void updateUserPostCount(AfterPostCreateEvent event) {
        if (event.getPost().isPublic()) {
            updateUserPostCount(event.getPost().getUserId(), event.getPost().isOriginPost());
        }
    }

    @Subscribe
    public void updateUserPostCount(AfterPostDestroyEvent event) {
        if (event.getPost().isPublic()) {
            updateUserPostCount(event.getPost().getUserId(), event.getPost().isOriginPost());
        }
    }

    private void updateUserPostCount(String userId, boolean isOrigin) {

        if (isOrigin) {
            mongoTemplate.updateFirst(
                    new Query(new Criteria(User.ID_KEY).is(userId)),
                    new Update()
                            .set(User.STATUSES_COUNT_KEY, statusService.countPost(userId, true, null))
                            .set(User.ORIGIN_POSTS_COUNT_KEY, statusService.countPost(userId, true, true)),
                    User.class);
        } else {
            mongoTemplate.updateFirst(
                    new Query(new Criteria(User.ID_KEY).is(userId)),
                    new Update()
                            .set(User.STATUSES_COUNT_KEY, statusService.countPost(userId, true, null))
                            .set(User.REPOSTS_COUNT_KEY, statusService.countPost(userId, null, false)),
                    User.class);
        }

    }

    @Subscribe
    public void updatePostRepostsCount(AfterPostCreateEvent event) {
        if (event.getPost().isRepost()) {
            updatePostRepostsCount(event.getPost().getOriginPostId());
        }
    }

    @Subscribe
    public void updatePostRepostsCount(AfterPostDestroyEvent event) {
        if (event.getPost().isRepost()) {
            updatePostRepostsCount(event.getPost().getOriginPostId());
        }
    }

    private void updatePostRepostsCount(String originPostId) {
        mongoTemplate.updateFirst(
                new Query(new Criteria(Post.ID_KEY).is(originPostId)),
                new Update().set(Post.REPOSTS_COUNT_KEY, statusService.countReposts(originPostId)),
                Post.class);
    }

    @Subscribe
    public void updatePostCommentsCount(AfterCommentCreateEvent event) {
        updatePostCommentsCount(event.getComment().getPostId());
    }

    @Subscribe
    public void updatePostCommentsCount(AfterCommentDestroyEvent event) {
        updatePostCommentsCount(event.getComment().getPostId());
    }

    private void updatePostCommentsCount(String postId) {
        mongoTemplate.updateFirst(
                new Query(new Criteria(Post.ID_KEY).is(postId)),
                new Update().set(Post.COMMENTS_COUNT_KEY, commentService.countPostComments(postId)),
                Post.class);
    }
}
