package com.lebo.schedule;

import com.lebo.entity.Statistics;
import com.lebo.service.StatisticsService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Calendar;
import java.util.Map;

public class StatisticsDailyClusterableJob extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(StatisticsDailyClusterableJob.class);

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
        StatisticsService statisticsService = applicationContext.getBean(StatisticsService.class);
        Map config = (Map) applicationContext.getBean("timerJobConfig");

        Calendar yestoday = Calendar.getInstance();
        yestoday.add(Calendar.DAY_OF_MONTH, -1);

        int year = yestoday.get(Calendar.YEAR);
        int month = yestoday.get(Calendar.MONTH) + 1;
        int date = yestoday.get(Calendar.DAY_OF_MONTH);

        //统计昨天数据
        Statistics statistics = statisticsService.createDaily(year, month, date);

        String nodeName = (String) config.get("nodeName");

        logger.info("统计昨天({}-{}-{}) :", year, month, date);
        logger.info("             user count : {}", statistics.getUserCount());
        logger.info("             post count : {}", statistics.getPostCount());
        logger.info("      origin post count : {}", statistics.getOriginPostCount());
        logger.info("      repost post count : {}", statistics.getRepostPostCount());
        logger.info("          comment count : {}", statistics.getCommentCount());
        logger.info("    audio comment count : {}", statistics.getAudioCommentCount());
        logger.info("    video comment count : {}", statistics.getVideoCommentCount());
        logger.info("   favorite count count : {}", statistics.getFavoriteCount());
        logger.info("     notification count : {}", statistics.getNotificationCount());
        logger.info("               im count : {}", statistics.getImCount());
        logger.info("          im text count : {}", statistics.getImTextCount());
        logger.info("         im audio count : {}", statistics.getImAudioCount());
        logger.info("         im video count : {}", statistics.getImVideoCount());
        logger.info("{} ms, by quartz cluster job on node {}.", statistics.getCollectTimeMillis(), nodeName);
    }
}