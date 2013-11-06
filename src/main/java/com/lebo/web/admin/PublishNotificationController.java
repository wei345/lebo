package com.lebo.web.admin;

import com.lebo.entity.Task;
import com.lebo.entity.User;
import com.lebo.service.TaskService;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送全网通知.
 *
 * @author: Wei Liu
 * Date: 13-10-24
 * Time: PM4:56
 */
@RequestMapping("/admin/task")
@Controller
public class PublishNotificationController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "apns-all-user", method = RequestMethod.GET)
    public String apnsAllUserForm(Model model) {
        List<Task> tasks = taskService.getTasksByType(Task.TYPE_VALUE_APNS_ALL_USER);

        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>(tasks.size());
        for (Task task : tasks) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", task.getNotificationText());
            map.put("count", task.getNotificationSentCount());
            //TODO 优化性能，只查screenName
            User user = accountService.getUser(task.getUserId());
            map.put("screenName", user.getScreenName());
            map.put("createdAt", task.getCreatedAt());
            map.put("id", task.getId());

            maps.add(map);
        }

        model.addAttribute("tasks", maps);
        model.addAttribute("avgPushTimeSeconds", taskService.getAvgPushTimeSeconds());
        model.addAttribute("oneSecondPushCount", taskService.getApnsAllUserQueueTotalThreadCount() / taskService.getAvgPushTimeSeconds());
        return "admin/task/apnsAllUser";
    }

    @RequestMapping(value = "apns-all-user", method = RequestMethod.POST)
    public String apnsAllUser(@RequestParam("text") String text,
                              RedirectAttributes redirectAttributes) {
        try {
            Task task = taskService.publishApnsAllUser(text);

            String message = new StringBuilder("成功. ")
                    .append(task.getNotificationSentCount())
                    .append(" 条通知已加入推送队列并开始推送, 预计推送完成需要 ")
                    .append((task.getNotificationSentCount() <= taskService.getApnsAllUserQueueTotalThreadCount()) ?
                            taskService.getAvgPushTimeSeconds() :
                            (task.getNotificationSentCount() * taskService.getAvgPushTimeSeconds()) / taskService.getApnsAllUserQueueTotalThreadCount())
                    .append(" 秒")
                    .toString();

            redirectAttributes.addFlashAttribute("success", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "发布通知失败");
        }
        return "redirect:/admin/task/apns-all-user";
    }
}
