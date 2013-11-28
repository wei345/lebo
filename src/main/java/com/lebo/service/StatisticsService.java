package com.lebo.service;

import com.lebo.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Wei Liu
 * Date: 13-10-25
 * Time: PM3:00
 */
@ManagedResource(objectName = "lebo:name=StatisticsService", description = "Statistics Service Management Bean")
@Service
public class StatisticsService extends AbstractMongoService {

    private Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    private SimpleDateFormat idFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 统计总数
     */
    public Statistics create() {

        long begin = System.currentTimeMillis();

        //统计
        Statistics statistics = new Statistics();
        statistics.setStatisticsDate(new Date());

        //用户
        statistics.setUserCount(mongoTemplate.count(new Query(), User.class));
        statistics.setAdminUserCount(mongoTemplate.count(new Query(new Criteria(User.ROLES_KEY).is(User.ROLES_ADMIN)), User.class));

        //帖子
        statistics.setPostCount(mongoTemplate.count(new Query(), Post.class));
        statistics.setOriginPostCount(mongoTemplate.count(new Query(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null)), Post.class));
        statistics.setRepostPostCount(mongoTemplate.count(new Query(new Criteria(Post.ORIGIN_POST_ID_KEY).ne(null)), Post.class));

        //评论
        statistics.setCommentCount(mongoTemplate.count(new Query(), Comment.class));
        statistics.setAudioCommentCount(mongoTemplate.count(new Query(new Criteria(Comment.AUDIO_KEY).ne(null)), Comment.class));
        statistics.setVideoCommentCount(mongoTemplate.count(new Query(new Criteria(Comment.VIDEO_KEY).ne(null)), Comment.class));

        //收藏
        statistics.setFavoriteCount(mongoTemplate.count(new Query(), Favorite.class));

        //关系
        statistics.setFriendshipCount(mongoTemplate.count(new Query(), Friendship.class));
        statistics.setFollowCount(mongoTemplate.count(new Query(new Criteria(Friendship.AFB_KEY).is(true)), Friendship.class) +
                mongoTemplate.count(new Query(new Criteria(Friendship.BFA_KEY).is(true)), Friendship.class));

        //标签
        statistics.setHashtagCount(mongoTemplate.count(new Query(), Hashtag.class));

        //通知
        statistics.setNotificationCount(mongoTemplate.count(new Query(), Notification.class));

        //定时任务
        statistics.setQuartzJobCount(mongoTemplate.count(new Query(), "quartz_jobs"));

        //即时通讯
        statistics.setImCount(mongoTemplate.count(new Query(), Im.class));
        statistics.setImTextCount(mongoTemplate.count(new Query(new Criteria(Im.TYPE_KEY).is(Im.TYPE_TEXT)), Im.class));
        statistics.setImAudioCount(mongoTemplate.count(new Query(new Criteria(Im.TYPE_KEY).is(Im.TYPE_AUDIO)), Im.class));
        statistics.setImVideoCount(mongoTemplate.count(new Query(new Criteria(Im.TYPE_KEY).is(Im.TYPE_VIDEO)), Im.class));

        //用时
        statistics.setCollectTimeMillis(System.currentTimeMillis() - begin);
        mongoTemplate.save(statistics, Statistics.COLLECTION_STATISTICS);

        return statistics;
    }

    /**
     * 统计一天内的数据.
     *
     * @param year
     * @param month 取值范围1-12
     * @param date
     */
    public Statistics createDaily(int year, int month, int date) {

        long begin = System.currentTimeMillis();

        //日期条件
        Calendar beginDate = Calendar.getInstance();
        beginDate.set(year, month - 1, date, 0, 0, 0);

        Calendar endDate = Calendar.getInstance();
        endDate.set(year, month - 1, date + 1, 0, 0, 0);

        Criteria dailyCriteria = new Criteria(Im.CREATED_AT_KEY).gte(beginDate.getTime()).lt(endDate.getTime());

        //检查是否已存在
        String id = idFormat.format(beginDate.getTime());
        Statistics statistics = mongoTemplate.findOne(new Query(new Criteria(Statistics.ID_KEY).is(id)), Statistics.class, Statistics.COLLECTION_STATISTICS_DAILY);
        if (statistics != null) {
            return statistics;
        }

        //统计
        statistics = new Statistics();
        statistics.setStatisticsDate(beginDate.getTime());
        statistics.setId(id);

        //用户
        statistics.setUserCount(mongoTemplate.count(new Query(dailyCriteria), User.class));
        statistics.setAdminUserCount(mongoTemplate.count(new Query(new Criteria(User.ROLES_KEY).is(User.ROLES_ADMIN)).addCriteria(dailyCriteria), User.class));

        //帖子
        statistics.setPostCount(mongoTemplate.count(new Query(dailyCriteria), Post.class));
        statistics.setOriginPostCount(mongoTemplate.count(new Query(new Criteria(Post.ORIGIN_POST_ID_KEY).is(null)).addCriteria(dailyCriteria), Post.class));
        statistics.setRepostPostCount(mongoTemplate.count(new Query(new Criteria(Post.ORIGIN_POST_ID_KEY).ne(null)).addCriteria(dailyCriteria), Post.class));

        //评论
        statistics.setCommentCount(mongoTemplate.count(new Query(dailyCriteria), Comment.class));
        statistics.setAudioCommentCount(mongoTemplate.count(new Query(new Criteria(Comment.AUDIO_KEY).ne(null)).addCriteria(dailyCriteria), Comment.class));
        statistics.setVideoCommentCount(mongoTemplate.count(new Query(new Criteria(Comment.VIDEO_KEY).ne(null)).addCriteria(dailyCriteria), Comment.class));

        //收藏
        statistics.setFavoriteCount(mongoTemplate.count(new Query(dailyCriteria), Favorite.class));

        //通知
        statistics.setNotificationCount(mongoTemplate.count(new Query(dailyCriteria), Notification.class));

        //用时
        statistics.setCollectTimeMillis(System.currentTimeMillis() - begin);

        //即时通讯
        statistics.setImCount(mongoTemplate.count(new Query(dailyCriteria), Im.class));

        if (statistics.getImCount() > 0) {

            statistics.setImTextCount(mongoTemplate.count(new Query(dailyCriteria).addCriteria(new Criteria(Im.TYPE_KEY).is(Im.TYPE_TEXT)), Im.class));
            statistics.setImAudioCount(mongoTemplate.count(new Query(dailyCriteria).addCriteria(new Criteria(Im.TYPE_KEY).is(Im.TYPE_AUDIO)), Im.class));
            statistics.setImVideoCount(mongoTemplate.count(new Query(dailyCriteria).addCriteria(new Criteria(Im.TYPE_KEY).is(Im.TYPE_VIDEO)), Im.class));

            //即时通讯使用人数
            String mapFunction = "function(){var u = {}; u[this.fromUserId] = 1; emit('from', u);}";
            String reduceFunction = "function (key, emits) {\n" +
                    "    var u = {};\n" +
                    "    for (var i = 0; i < emits.length; i++) {\n" +
                    "       for(var p in emits[i]){\n" +
                    "          u[p] = u[p] ? (u[p] + emits[i][p]) : emits[i][p];\n" +
                    "       }\n" +
                    "    }\n" +
                    "    return u;\n" +
                    "}";
            String finalizeFunction = "function(key, reducedValue) {" +
                    "var count = 0; " +
                    "for(var p in reducedValue){count++;} " +
                    "return count;" +
                    "}";
            MapReduceResults<Map> mapReduceResults = mongoTemplate.mapReduce(
                    new Query(dailyCriteria),
                    mongoTemplate.getCollectionName(Im.class),
                    mapFunction,
                    reduceFunction,
                    new MapReduceOptions().outputTypeInline().finalizeFunction(finalizeFunction),
                    Map.class);

            Iterator<Map> it = mapReduceResults.iterator();
            if (it.hasNext()) {
                statistics.setImFromUserCount(
                        Long.parseLong(
                                StringUtils.substringBefore(
                                        it.next().get("value").toString(), ".")));
            } else {
                statistics.setImFromUserCount(0L);
            }
        } else {
            statistics.setImTextCount(0L);
            statistics.setImAudioCount(0L);
            statistics.setImVideoCount(0L);
            statistics.setImFromUserCount(0L);
        }

        //保存
        mongoTemplate.save(statistics, Statistics.COLLECTION_STATISTICS_DAILY);

        return statistics;
    }

    public List<Statistics> get(Date startDate, Date endDate) {
        Query query = new Query();
        query.addCriteria(new Criteria(Statistics.STATISTICS_DATE_KEY).gte(startDate).lte(endDate));
        return mongoTemplate.find(query, Statistics.class, Statistics.COLLECTION_STATISTICS);
    }

    public List<Statistics> getDaily(Date startDate, Date endDate) {
        Query query = new Query();
        query.addCriteria(new Criteria(Statistics.STATISTICS_DATE_KEY).gte(startDate).lte(endDate));
        return mongoTemplate.find(query, Statistics.class, Statistics.COLLECTION_STATISTICS_DAILY);
    }

    //-- JMX --

    @ManagedOperation(description = "重新计算每日的历史数据, 可用于修复没有每日历史，或每日历史不完整的情况")
    public void dailyHistory() {
        long beginTime = System.currentTimeMillis();
        logger.info("重新计算每日的历史数据 : 开始");

        //查数据最早日期
        Query query = new Query();

        Calendar leboBeginYear = Calendar.getInstance();
        leboBeginYear.set(2013, 0, 1, 0, 0, 0);
        query.addCriteria(new Criteria(User.CREATED_AT_KEY).gt(leboBeginYear.getTime())); //避免从1970年开始

        query.fields().include(User.CREATED_AT_KEY);
        query.with(new Sort(Sort.Direction.ASC, User.CREATED_AT_KEY));
        query.limit(1);

        List<User> userList = mongoTemplate.find(query, User.class);

        if (userList.size() > 0) {
            //统计最早日期至现在
            Calendar statisticsDate = Calendar.getInstance();
            statisticsDate.setTime(userList.get(0).getCreatedAt());
            statisticsDate.set(Calendar.HOUR, 0);
            statisticsDate.set(Calendar.MINUTE, 0);
            statisticsDate.set(Calendar.SECOND, 0);

            Long now = new Date().getTime();

            while (statisticsDate.getTimeInMillis() < now) {

                int year = statisticsDate.get(Calendar.YEAR);
                int month = statisticsDate.get(Calendar.MONTH) + 1;
                int date = statisticsDate.get(Calendar.DAY_OF_MONTH);

                logger.debug("重新计算每日的历史数据 : {}-{}-{}", year, month, date);

                createDaily(year, month, date);

                statisticsDate.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        logger.info("重新计算每日的历史数据 : 完成，用时 {} s", (System.currentTimeMillis() - beginTime) / 1000);
    }
}
