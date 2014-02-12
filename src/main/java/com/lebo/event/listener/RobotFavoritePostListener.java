package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.entity.Setting;
import com.lebo.entity.User;
import com.lebo.event.AfterPostCreateEvent;
import com.lebo.schedule.FavoritePostClusterableJob;
import com.lebo.service.RobotService;
import com.lebo.service.SettingService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author: Wei Liu
 * Date: 14-2-11
 * Time: PM6:39
 */
@Component
public class RobotFavoritePostListener {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private SettingService settingService;
    @Autowired
    private RobotService robotService;

    private Logger logger = LoggerFactory.getLogger(RobotFavoritePostListener.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Subscribe
    public void favoritePost(AfterPostCreateEvent event) throws SchedulerException {

        Setting setting = settingService.getSetting();
        if (setting.getRobotAutoFavoriteEnable() && event.getPost().getOriginPostId() == null) {

            //选取机器人
            List<User> robots;
            List<User> selectedRobots;
            int count = random(setting.getRobotAutoFavoriteRobotCountFrom(), setting.getRobotAutoFavoriteRobotCountTo());
            Random r = new Random();

            if (StringUtils.isBlank(setting.getRobotAutoFavoriteRobotGroup())) {
                robots = robotService.getAllRobots();
            } else {
                robots = robotService.getRobotsByGroup(setting.getRobotAutoFavoriteRobotGroup());
            }

            if (count >= robots.size()) {
                selectedRobots = robots;
            }
            //随机选取count个机器人
            else {
                selectedRobots = new ArrayList<User>(count);
                for (int i = 0; i < count; i++) {
                    selectedRobots.add(robots.remove(r.nextInt(robots.size())));
                }
            }

            //时间范围
            int minute = random(setting.getRobotAutoFavoriteTimeInMinuteFrom(), setting.getRobotAutoFavoriteTimeInMinuteTo());

            //创建任务
            for (User robot : selectedRobots) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, r.nextInt(minute) + 1);
                favoritePost(robot.getId(), event.getPost().getId(), calendar.getTime());
            }
        }
    }

    /**
     * 返回 <code>from</code>(包含)至<code>to</code>(包含)之间的随机整数
     */
    private int random(int from, int to) {
        return new Random().nextInt(to - from + 1) + from;
    }

    private void favoritePost(String userId, String postId, Date jobDate) throws SchedulerException {

        Map<String, Object> jobData = new HashMap<String, Object>(2);
        jobData.put("userId", userId);
        jobData.put("postId", postId);

        String prefix = "user_" + userId + "_favorite_post_" + postId + "_";
        String group = "ROBOT";

        JobDetail jobDetail = newJob(FavoritePostClusterableJob.class)
                .withIdentity(
                        prefix + "jobDetail",
                        group)
                .setJobData(new JobDataMap(jobData))
                .build();

        Trigger trigger = newTrigger()
                .withIdentity(prefix + "trigger", group)
                .startAt(jobDate)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        logger.debug("已创建任务 {}, 将在 {} 执行", jobDetail.getKey(), sdf.format(jobDate));
    }

}
