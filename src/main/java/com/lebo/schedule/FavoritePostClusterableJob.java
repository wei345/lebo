package com.lebo.schedule;

import com.lebo.service.FavoriteService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 收藏(也称喜欢、赞)帖子定时任务。
 * 当新帖子被创建时，系统创建多个定时任务，在未来一段时间内机器人收藏帖子。
 *
 * @author: Wei Liu
 * Date: 14-2-10
 * Time: PM3:30
 */
public class FavoritePostClusterableJob extends QuartzJobBean {
    private Logger logger = LoggerFactory.getLogger(FavoritePostClusterableJob.class);

    private ApplicationContext applicationContext;
    private FavoriteService favoriteService;
    private String nodeName;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        String userId = (String) context.getMergedJobDataMap().get("userId");
        String postId = (String) context.getMergedJobDataMap().get("postId");

        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(postId)) {
            getFavoriteService().create(userId, postId);
            logger.info("用户 {} 收藏了帖子 {}, by quartz cluster job on node {}", userId, postId, nodeName);
        }
    }

    /**
     * 从SchedulerFactoryBean注入的applicationContext.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public FavoriteService getFavoriteService() {
        if (favoriteService == null) {
            favoriteService = applicationContext.getBean(FavoriteService.class);
        }
        return favoriteService;
    }
}
