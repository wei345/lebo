package com.lebo.schedule;

import com.lebo.service.StatusService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Map;

/**
 * 集群内定时更新热门帖子。
 *
 * @author: Wei Liu
 * Date: 13-8-15
 * Time: PM1:50
 */
public class HotPostClusterableJob extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(HotPostClusterableJob.class);

    private ApplicationContext applicationContext;

    /**
     * 从SchedulerFactoryBean注入的applicationContext.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 更新热门帖子列表.
     */
    @Override
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
        StatusService statusService = applicationContext.getBean(StatusService.class);
        Map config = (Map) applicationContext.getBean("timerJobConfig");

        statusService.refreshHotPosts();
        String nodeName = (String) config.get("nodeName");

        logger.info("hotPosts updated, by quartz cluster job on node {}.", nodeName);
    }
}
