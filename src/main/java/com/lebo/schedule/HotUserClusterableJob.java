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
 * 被Spring的Quartz JobDetailBean定时执行的Job类, 支持持久化到数据库实现Quartz集群.
 * <p/>
 * 因为需要被持久化, 不能有用XXService等不能被持久化的成员变量,
 * 只能在每次调度时从QuartzJobBean注入的applicationContext中动态取出.
 */
public class HotUserClusterableJob extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(HotUserClusterableJob.class);

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

        accountService.refreshHotUsers();
        String nodeName = (String) config.get("nodeName");

        logger.info("hotUsers updated, by quartz cluster job on node {}.", nodeName);
    }
}
