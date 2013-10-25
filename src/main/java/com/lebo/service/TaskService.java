package com.lebo.service;

import com.lebo.entity.FileInfo;
import com.lebo.entity.Task;
import com.lebo.entity.User;
import com.lebo.jms.ApnsMessageProducer;
import com.lebo.repository.TaskDao;
import com.lebo.service.account.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: Wei Liu
 * Date: 13-9-27
 * Time: PM4:50
 */
@Service
public class TaskService extends AbstractMongoService {
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ApnsMessageProducer apnsAllUserMessageProducer;
    @Value("${jms.apnsAllUserQueueTotalThreadCount}")
    private int apnsAllUserQueueTotalThreadCount;
    @Value("${apns.avgPushTimeSeconds}")
    private int avgPushTimeSeconds;

    public static final String UPLOAD_VIDEO_EXT = "mp4";
    public static final String UPLOAD_PHOTO_EXT = "jpg";
    private static final String OSS_PREFIX_PUBLISH_VIDEO = "task/publish-video";

    private SimpleDateFormat publishVideoSlugSdf = new SimpleDateFormat("yyyyMMdd-HHmm");

    public Task getTask(String id) {
        return taskDao.findOne(id);
    }

    public void saveTask(Task entity) {
        taskDao.save(entity);
    }

    public void deleteTask(String id) {
        taskDao.delete(id);
    }

    public List<Task> getAllTask() {
        return taskDao.findAll();
    }

    /**
     * 查询待执行的到期任务
     */
    public List<Task> getDueTask(Date time, String type) {
        Query query = new Query();
        query.addCriteria(new Criteria(Task.SCHEDULED_AT_KEY).lte(time));
        query.addCriteria(new Criteria(Task.STATUS_KEY).is(Task.STATUS_VALUE_TODO));
        query.addCriteria(new Criteria(Task.TYPE_KEY).is(type));
        return mongoTemplate.find(query, Task.class);
    }

    public void createTaskPublishVideo(String userId, String text, FileInfo video, FileInfo videoFirstFrame, String source, Date scheduledAt) {
        //创建Task
        Task task = new Task();
        task.setCreatedAt(new Date());
        task.setId(newMongoId(task.getCreatedAt()));
        task.setUserId(accountService.getCurrentUserId());
        task.setTitle("定时发布视频");
        task.setType(Task.TYPE_VALUE_PUBLISH_VIDEO);
        task.setStatus(Task.STATUS_VALUE_TODO);
        task.setScheduledAt(scheduledAt);

        //保存视频文件
        String scheduledAtStr = publishVideoSlugSdf.format(scheduledAt);
        video.setKey(generateFileId(OSS_PREFIX_PUBLISH_VIDEO, task.getId(), scheduledAtStr + "-video", video.getLength(), video.getContentType(), video.getFilename()));
        videoFirstFrame.setKey(generateFileId(OSS_PREFIX_PUBLISH_VIDEO, task.getId(), scheduledAtStr + "-video-first-frame", videoFirstFrame.getLength(), videoFirstFrame.getContentType(), videoFirstFrame.getFilename()));
        fileStorageService.save(video, videoFirstFrame);

        //视频信息
        task.setVideoUserId(userId);
        task.setVideo(video);
        task.setVideoFirstFrame(videoFirstFrame);
        task.setVideoText(text);
        task.setVideoSource(source);

        taskDao.save(task);
    }

    public List<Task> getPublishVideoTodoTask() {
        Query query = new Query();
        query.addCriteria(new Criteria(Task.STATUS_KEY).is(Task.STATUS_VALUE_TODO));
        query.addCriteria(new Criteria(Task.TYPE_KEY).is(Task.TYPE_VALUE_PUBLISH_VIDEO));
        query.with(new Sort(Sort.Direction.ASC, Task.SCHEDULED_AT_KEY));
        return mongoTemplate.find(query, Task.class);
    }

    public void deleteTaskPublishVideo(String id) {
        Task task = taskDao.findOne(id);
        if (task.getType().equals(Task.TYPE_VALUE_PUBLISH_VIDEO)) {
            //注意: 已完成的任务视频key已指向帖子视频，不可删除视频文件
            if (task.getStatus().equals(Task.STATUS_VALUE_TODO)) {
                fileStorageService.delete(task.getVideo().getKey());
                fileStorageService.delete(task.getVideoFirstFrame().getKey());
            }
            taskDao.delete(id);
        }
    }

    /**
     * 给所有ios用户发送推送通知
     */
    public Task publishApnsAllUser(String text) {
        Task task = new Task(Task.TYPE_VALUE_APNS_ALL_USER,
                accountService.getCurrentUserId(),
                new Date(),
                "发布通知");

        //获取用户
        Query query = new Query();
        query.addCriteria(new Criteria(User.APNS_PRODUCTION_TOKEN_KEY).ne(""))
                .fields().include(User.APNS_PRODUCTION_TOKEN_KEY)
                .include(User.SCREEN_NAME_KEY);
        List<User> users = mongoTemplate.find(query, User.class);

        //发送
        Set<String> allToken = new HashSet<String>(users.size());
        for (User user : users) {
            if (StringUtils.isNotBlank(user.getApnsProductionToken()) &&
                    !allToken.contains(user.getApnsProductionToken())) {
                allToken.add(user.getApnsProductionToken());
                apnsAllUserMessageProducer.sendNotificationQueue(text, user.getApnsProductionToken(), user);
            }
        }

        //保存任务结果
        task.setNotificationText(text);
        task.setNotificationSentCount(allToken.size());

        taskDao.save(task);
        return task;
    }

    public List<Task> getTasksByType(String type) {
        Query query = new Query();
        query.addCriteria(new Criteria(Task.TYPE_KEY).is(type))
                .with(new Sort(Sort.Direction.DESC, Task.ID_KEY));
        return mongoTemplate.find(query, Task.class);
    }

    public int getApnsAllUserQueueTotalThreadCount() {
        return apnsAllUserQueueTotalThreadCount;
    }

    public int getAvgPushTimeSeconds() {
        return avgPushTimeSeconds;
    }
}
