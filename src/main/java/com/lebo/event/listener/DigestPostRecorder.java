package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.entity.Post;
import com.lebo.entity.User;
import com.lebo.event.AfterPostCreateEvent;
import com.lebo.event.AfterPostDestroyEvent;
import com.lebo.event.BeforePostCreateEvent;
import com.lebo.service.SettingService;
import com.lebo.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * @author: Wei Liu
 * Date: 13-8-3
 * Time: PM1:42
 */
@Component
public class DigestPostRecorder {
    @Autowired
    private SettingService settingService;
    @Autowired
    protected MongoTemplate mongoTemplate;
    @Autowired
    private StatusService statusService;

    @Subscribe
    public void markDigest(BeforePostCreateEvent event) {
        //被特定乐播账号转发，记为精华
        if (isMarkDigest(event.getPost())) {
            //转发贴和原帖都标记为精华
            event.getPost().setDigest(true);
            mongoTemplate.updateFirst(new Query(new Criteria(Post.ID_KEY).is(event.getPost().getOriginPostId())),
                    new Update().set(Post.DIGEST_KEY, true), Post.class);
        }
        //其他记为非精华
        else {
            event.getPost().setDigest(false);
        }
    }

    @Subscribe
    public void increaseUserDigestCount(AfterPostCreateEvent event) {
        if (isMarkDigest(event.getPost())) {
            //更新用户精华帖子计数
            updateUserDigestCount(event.getPost().getOriginPostUserId());
        }
    }

    @Subscribe
    public void decreaseUserDigestCount(AfterPostDestroyEvent event) {
        if (event.getPost().getDigest()) {
            //如果当前删除的是加精的转发贴，则认为是取消加精
            if (event.getPost().getOriginPostId() != null) {
                mongoTemplate.updateFirst(new Query(new Criteria(Post.ID_KEY).is(event.getPost().getOriginPostId())),
                        new Update().set(Post.DIGEST_KEY, false), Post.class);
            }

            //更新用户精华帖子计数
            updateUserDigestCount(event.getPost().getOriginPostUserId());
        }
    }

    private void updateUserDigestCount(String userId) {
        int digestCount = statusService.countUserDigest(userId);
        mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(userId)),
                new Update().set(User.DIGEST_COUNT_KEY, digestCount), User.class);
    }

    private boolean isMarkDigest(Post post) {
        return post.getOriginPostId() != null
                && post.getUserId().equals(settingService.getSetting().getOfficialAccountId());
    }
}
