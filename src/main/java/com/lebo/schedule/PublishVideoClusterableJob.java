package com.lebo.schedule;

import com.lebo.entity.Post;
import com.lebo.entity.Task;
import com.lebo.service.FileContentUrlUtils;
import com.lebo.service.FileStorageService;
import com.lebo.service.StatusService;
import com.lebo.service.TaskService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * 定时发布视频.
 *
 * @author: Wei Liu
 * Date: 13-9-28
 * Time: PM4:30
 */
public class PublishVideoClusterableJob extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(PublishVideoClusterableJob.class);

    private ApplicationContext applicationContext;

    /**
     * 从SchedulerFactoryBean注入的applicationContext.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        TaskService taskService = applicationContext.getBean(TaskService.class);

        logger.debug("定时发布视频 : 正在获取任务列表..");
        List<Task> tasks = taskService.getDueTask(new Date(), Task.TYPE_VALUE_PUBLISH_VIDEO);
        logger.debug("定时发布视频 : 获得 {} 项", tasks.size());

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            logger.debug("定时发布视频 : 正在发布视频 {}/{}", i + 1, tasks.size());
            publishVideo(task);
            task.setStatus(Task.STATUS_VALUE_DONE);
            //保存，状态为已完成，同时视频key已变为已发布帖子里的视频key
            taskService.saveTask(task);
        }
    }

    private void publishVideo(Task task) {
        logger.info("定时发布视频 : 正在发布，key : {}, text : {}", task.getVideo().getKey(), task.getVideoText());

        StatusService statusService = applicationContext.getBean(StatusService.class);
        FileStorageService fileStorageService = applicationContext.getBean(FileStorageService.class);

        HttpURLConnection videoConn = null;
        HttpURLConnection videoFirstFrameConn = null;
        try {
            videoConn = (HttpURLConnection) new URL(FileContentUrlUtils.getContentUrl(task.getVideo().getKey())).openConnection();
            videoFirstFrameConn = (HttpURLConnection) new URL(FileContentUrlUtils.getContentUrl(task.getVideoFirstFrame().getKey())).openConnection();

            task.getVideo().setContent(videoConn.getInputStream());
            task.getVideoFirstFrame().setContent(videoFirstFrameConn.getInputStream());

            //调用 statusService.createPost(..) 之后key会发生变化，先取到，创建视频之后，用此key删除文件
            String taskVideoKey = task.getVideo().getKey();
            String taskVideoFirstFrameKey = task.getVideoFirstFrame().getKey();

            //发布视频
            Post post = statusService.createPost(task.getVideoUserId(), task.getVideoText(), task.getVideo(), task.getVideoFirstFrame(), null, task.getVideoSource(), Post.ACL_PUBLIC);

            //删除任务文件
            fileStorageService.delete(taskVideoKey);
            fileStorageService.delete(taskVideoFirstFrameKey);

            logger.info("定时发布视频 : 完成，postId : {}", post.getId());
        } catch (Exception e) {
            logger.warn("定时发布视频 : 失败，key : {}, text : {}", task.getVideo().getKey(), task.getVideoText());
        } finally {
            if (videoConn != null) {
                videoConn.disconnect();
            }
            if (videoFirstFrameConn != null) {
                videoFirstFrameConn.disconnect();
            }
        }
    }
}
