package com.lebo.schedule;

import com.lebo.entity.Comment;
import com.lebo.entity.Post;
import com.lebo.entity.Task;
import com.lebo.repository.PostDao;
import com.lebo.service.*;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springside.modules.mapper.JsonMapper;

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
public class AdminTaskClusterableJob extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(AdminTaskClusterableJob.class);

    private ApplicationContext applicationContext;

    private CommentService commentService;

    private JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    private PostDao postDao;

    /**
     * 从SchedulerFactoryBean注入的applicationContext.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        TaskService taskService = applicationContext.getBean(TaskService.class);

        logger.debug("定时发布 : 正在获取任务列表..");
        List<Task> tasks = taskService.getDueTask(new Date());
        logger.debug("定时发布 : 获得 {} 项", tasks.size());

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            switch (task.getType()) {
                case PUBLISH_VIDEO:
                    logger.debug("定时发布 : 正在发布视频 {}/{}", i + 1, tasks.size());
                    publishVideo(task);
                    break;
                case ROBOT_COMMENT:
                    logger.debug("定时发布 : 正在发布评论 {}/{}", i + 1, tasks.size());
                    publishComment(task);
                    break;
            }

            task.setStatus(Task.Status.DONE);
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

            //调用 statusService.createPost(..) 之后video.key会发生变化，先取到，创建视频之后，用此key删除文件
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

    public void publishComment(Task task) {
        Task.RobotComment robotComment = jsonMapper.fromJson(task.getTaskData(), Task.RobotComment.class);

        if (getPostDao().exists(robotComment.getPostId())) { //帖子存在
            Comment comment = new Comment();
            comment.setUserId(robotComment.getUserId());
            comment.setPostId(robotComment.getPostId());
            comment.setText(robotComment.getText());
            comment.setCreatedAt(task.getScheduledAt());

            getCommentService().create(comment, null, null, null);
        }
    }

    public CommentService getCommentService() {
        if (commentService == null) {
            commentService = applicationContext.getBean(CommentService.class);
        }
        return commentService;
    }

    public PostDao getPostDao() {
        if (postDao == null) {
            postDao = applicationContext.getBean(PostDao.class);
        }
        return postDao;
    }
}
