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
 * 导演等级.
 *
 * @author: Wei Liu
 * Date: 13-9-2
 * Time: PM6:09
 */
@Component
public class UserLevelRecorder {
    @Autowired
    private DigestPostRecorder digestPostRecorder;
    @Autowired
    private StatusService statusService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Subscribe
    public void updateUserLevel(AfterPostCreateEvent event) {
        if (digestPostRecorder.isMarkDigest(event.getPost())) {
            updateUserLevel(event.getPost().getOriginPostUserId());
        }
    }

    @Subscribe
    public void updateUserLevel(AfterPostDestroyEvent event) {
        if (event.getPost().getDigest()) {
            //取消加精
            if (event.getPost().getOriginPostId() != null && event.getPost().getOriginPostUserId() != null) {
                updateUserLevel(event.getPost().getOriginPostUserId());
            }
            //精华贴被删除
            else {
                updateUserLevel(event.getPost().getUserId());
            }
        }
    }

    void updateUserLevel(String userId) {
        int level = getLevel(statusService.countUserDigest(userId));
        mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(userId)),
                new Update().set(User.LEVEL_KEY, level), User.class);
    }

    int getLevel(int digestCount) {
        int level = 0;
        int count = 0;
        while (count + level + 1 <= digestCount) {
            level++;
            count += level;
            if (level == 60) { //目录最高60级
                return level;
            }
        }
        return level;
    }
}
