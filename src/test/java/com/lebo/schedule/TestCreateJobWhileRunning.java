package com.lebo.schedule;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author: Wei Liu
 * Date: 14-2-10
 * Time: PM2:05
 */
public class TestCreateJobWhileRunning extends SpringContextTestCase {

    @Autowired
    private Scheduler scheduler;

    private Logger logger = LoggerFactory.getLogger(TestCreateJobWhileRunning.class);

    @Test
    public void addJob() throws SchedulerException {
        Map<String, Object> jobData = new HashMap<String, Object>(2);
        jobData.put("a", "aa");
        jobData.put("b", "bb");

        JobDetail jobDetail = newJob(FavoritePostClusterableJob.class)
                .withIdentity("job1")
                .setJobData(new JobDataMap(jobData))
                .build();

        Date runTime = new Date();
        logger.info(jobDetail.getKey() + " will run at: " + runTime);

        Trigger trigger = newTrigger().withIdentity("trigger1").startAt(runTime).build();

        try {
            //让quartz有时间清理数据库中过期和无效任务，因为我可能加断点后结束程序，导致数据库残留测试任务
            Thread.sleep(3L * 1000L);
            // executing...
        } catch (Exception e) {
            //
        }

        scheduler.scheduleJob(jobDetail, trigger);

        try {
            // wait 65 seconds to show job
            Thread.sleep(65L * 1000L);
            // executing...
        } catch (Exception e) {
            //
        }
    }

}
