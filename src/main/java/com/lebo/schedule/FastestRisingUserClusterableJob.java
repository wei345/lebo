package com.lebo.schedule;

import com.lebo.service.account.AccountService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Map;

/**
 * 定时刷新上升最快用户列表.
 */
public class FastestRisingUserClusterableJob extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(FastestRisingUserClusterableJob.class);

    private ApplicationContext applicationContext;

    /**
     * 从SchedulerFactoryBean注入的applicationContext.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 更新推荐关注列表.
     */
    @Override
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
        AccountService accountService = applicationContext.getBean(AccountService.class);
        Map config = (Map) applicationContext.getBean("timerJobConfig");

        accountService.refreshFastestRisingUsers();
        String nodeName = (String) config.get("nodeName");

        logger.info("已更新 上升最快用户列表, by quartz cluster job on node {}.", nodeName);
    }
}