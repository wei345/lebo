package com.lebo.service;

import com.lebo.entity.*;
import com.lebo.repository.StatisticsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-10-25
 * Time: PM3:00
 */
@Service
public class StatisticsService extends AbstractMongoService {

    @Autowired
    private StatisticsDao statisticsDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Statistics create() {

        long begin = System.currentTimeMillis();
        Statistics statistics = new Statistics();

        statistics.setTotalUserCount(mongoTemplate.count(new Query(), User.class));
        statistics.setAdminUserCount(mongoTemplate.count(new Query(new Criteria(User.ROLES_KEY).is("admin")), User.class));

        statistics.setTotalPostCount(mongoTemplate.count(new Query(), Post.class));
        statistics.setOriginPostCount(mongoTemplate.count(new Query(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null)), Post.class));
        statistics.setRepostPostCount(mongoTemplate.count(new Query(new Criteria(Post.ORIGIN_POST_ID_KEY).ne(null)), Post.class));

        statistics.setTotalCommentCount(mongoTemplate.count(new Query(), Comment.class));
        statistics.setAudioCommentCount(mongoTemplate.count(new Query(new Criteria(Comment.AUDIO_KEY).ne(null)), Comment.class));
        statistics.setVideoCommentCount(mongoTemplate.count(new Query(new Criteria(Comment.VIDEO_KEY).ne(null)), Comment.class));

        statistics.setTotalFavoriteCount(mongoTemplate.count(new Query(), Favorite.class));
        statistics.setTotalFriendshipCount(mongoTemplate.count(new Query(), Friendship.class));
        statistics.setTotalFollowCount(mongoTemplate.count(new Query(new Criteria(Friendship.AFB_KEY).is(true)), Friendship.class) +
                mongoTemplate.count(new Query(new Criteria(Friendship.BFA_KEY).is(true)), Friendship.class));
        statistics.setHashtagCount(mongoTemplate.count(new Query(), Hashtag.class));
        statistics.setNotificationCount(mongoTemplate.count(new Query(), Notification.class));
        statistics.setQuartzJobCount(mongoTemplate.count(new Query(), "quartz_jobs"));

        statistics.setCollectTimeMillis(System.currentTimeMillis() - begin);
        statistics.setCreatedAt(new Date());

        statisticsDao.save(statistics);
        return statistics;
    }
}
