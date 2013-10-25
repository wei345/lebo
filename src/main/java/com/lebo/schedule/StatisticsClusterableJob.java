package com.lebo.schedule;

import com.lebo.entity.Statistics;
import com.lebo.service.StatisticsService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Map;

public class StatisticsClusterableJob extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(StatisticsClusterableJob.class);

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
        StatisticsService statisticsService = applicationContext.getBean(StatisticsService.class);
        Map config = (Map) applicationContext.getBean("timerJobConfig");

        Statistics statistics = statisticsService.create();
        String nodeName = (String) config.get("nodeName");

        logger.info("统计 :");
        logger.info("             user count : {}", statistics.getTotalUserCount());
        logger.info("             post count : {}", statistics.getTotalPostCount());
        logger.info("      origin post count : {}", statistics.getOriginPostCount());
        logger.info("      repost post count : {}", statistics.getRepostPostCount());
        logger.info("          comment count : {}", statistics.getTotalCommentCount());
        logger.info("    audio comment count : {}", statistics.getAudioCommentCount());
        logger.info("    video comment count : {}", statistics.getVideoCommentCount());
        logger.info("   favorite count count : {}", statistics.getTotalFavoriteCount());
        logger.info("           follow count : {}", statistics.getTotalFollowCount());
        logger.info("          hashtag count : {}", statistics.getHashtagCount());
        logger.info("     notification count : {}", statistics.getNotificationCount());
        logger.info("{} ms, by quartz cluster job on node {}.", statistics.getCollectTimeMillis(), nodeName);
    }
}