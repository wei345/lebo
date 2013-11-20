package com.lebo.schedule;

import com.lebo.service.account.AccountService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Map;

public class Top50UserClusterableJob extends QuartzJobBean {

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

        accountService.refreshTop50Users();
        String nodeName = (String) config.get("nodeName");

        logger.info("已更新 Top50用户列表, by quartz cluster job on node {}.", nodeName);
    }
}