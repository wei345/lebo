package com.lebo.schedule;

import com.lebo.service.UploadService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Map;

/**
 * 清理过期的上传地址和上传的文件。
 *
 * @author: Wei Liu
 * Date: 13-11-7
 * Time: PM12:09
 */
public class CleanExpireUploadUrlClusterableJob extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(CleanExpireUploadUrlClusterableJob.class);

    private ApplicationContext applicationContext;

    /**
     * 从SchedulerFactoryBean注入的applicationContext.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
        long beginTime = System.currentTimeMillis();

        UploadService uploadService = applicationContext.getBean(UploadService.class);
        Map config = (Map) applicationContext.getBean("timerJobConfig");

        int count = uploadService.cleanExpireUrl();

        String nodeName = (String) config.get("nodeName");

        logger.info("已清理过期的上传URL {}, by quartz cluster job on node {}, {} ms.", count, nodeName, (System.currentTimeMillis() - beginTime));
    }
}
