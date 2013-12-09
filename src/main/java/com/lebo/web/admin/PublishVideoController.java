package com.lebo.web.admin;

import com.lebo.entity.FileInfo;
import com.lebo.entity.Post;
import com.lebo.entity.Task;
import com.lebo.entity.User;
import com.lebo.service.FileContentUrlUtils;
import com.lebo.service.StatusService;
import com.lebo.service.TaskService;
import com.lebo.service.account.AccountService;
import com.lebo.util.ContentTypeMap;
import com.lebo.util.VideoUtils;
import com.lebo.web.ControllerUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/admin/task/publish-video")
public class PublishVideoController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StatusService statusService;

    private SimpleDateFormat scheduleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    private Logger logger = LoggerFactory.getLogger(PublishVideoController.class);

    /**
     * 发布视频，表单
     */
    @RequestMapping(method = RequestMethod.GET)
    public String publishVideoForm(Model model) {
        List<Task> taskList = taskService.getPublishVideoTodoTask();

        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>(taskList.size());

        for (Task task : taskList) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("videoUrl", FileContentUrlUtils.getContentUrl(task.getVideo().getKey()));

            map.put("videoFirstFrameUrl", FileContentUrlUtils.getContentUrl(task.getVideoFirstFrame().getKey()));

            map.put("duration", task.getVideo().getDuration());

            //TODO 优化性能，只查screenName
            User user = accountService.getUser(task.getVideoUserId());
            map.put("screenName", user.getScreenName());

            map.put("text", task.getVideoText());

            map.put("scheduledAt", scheduleDateFormat.format(task.getScheduledAt()));

            map.put("id", task.getId());

            tasks.add(map);
        }

        model.addAttribute("tasks", tasks);
        return "admin/task/publishVideo";
    }

    /**
     * 发布视频，提交
     */
    @RequestMapping(method = RequestMethod.POST)
    public String publishVideo(@RequestParam(value = "screenName") String screenName,
                               @RequestParam(value = "video") MultipartFile video,
                               @RequestParam(value = "text") String text,
                               @RequestParam(value = "publishDate", required = false) String publishDate,
                               @RequestParam(value = "publishTime", required = false) String publishTime,
                               RedirectAttributes redirectAttributes) {
        //无论成功或失败都返回同一view
        String publisVideoView = "redirect:/admin/task/publish-video";

        boolean isSchedule = StringUtils.isNotBlank(publishDate);

        //发生错误时回填表单数据
        redirectAttributes.addFlashAttribute("screenName", screenName);
        redirectAttributes.addFlashAttribute("text", text);
        redirectAttributes.addFlashAttribute("publishDate", publishDate);
        redirectAttributes.addFlashAttribute("publishTime", publishTime);

        //检查视频格式
        if (!TaskService.UPLOAD_VIDEO_EXT.equals(FilenameUtils.getExtension(video.getOriginalFilename()))) {
            redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_ERROR_KEY, String.format("视频文件格式错误，只允许 %s", TaskService.UPLOAD_VIDEO_EXT));
            return publisVideoView;
        }

        //查找用户ID
        String userId;
        try {
            userId = accountService.getUserId(null, screenName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_ERROR_KEY, String.format("失败，找不到用户[%s]", screenName));
            return publisVideoView;
        }

        try {
            // 接收文件，获得视频第一帧截图
            File tempVideoFile = File.createTempFile(Task.TYPE_VALUE_PUBLISH_VIDEO, "." + TaskService.UPLOAD_VIDEO_EXT);
            video.transferTo(tempVideoFile);

            //截图，视频第一帧
            File tempPhotoFile = File.createTempFile(Task.TYPE_VALUE_PUBLISH_VIDEO, "." + TaskService.UPLOAD_PHOTO_EXT);
            VideoUtils.writeVideoFirstFrame(tempVideoFile, tempPhotoFile);

            FileInfo videoFileInfo = new FileInfo(new FileInputStream(tempVideoFile), video.getContentType(), tempVideoFile.length(), tempVideoFile.getName());
            FileInfo videoFirstFrameInfo = new FileInfo(new FileInputStream(tempPhotoFile), ContentTypeMap.getContentType(FilenameUtils.getExtension(tempPhotoFile.getName())), tempPhotoFile.length(), tempPhotoFile.getName());

            //视频时长
            videoFileInfo.setDuration((long) VideoUtils.getVideoDurationInSeconds(tempVideoFile.getAbsolutePath()));

            //定时发布
            if (isSchedule) {

                Date scheduledAt;

                try {

                    scheduledAt = scheduleDateFormat.parse(publishDate + " " + publishTime);

                } catch (ParseException ex) {

                    redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_ERROR_KEY, "日期格式错误，正确格式为: " + scheduleDateFormat.toPattern());

                    return publisVideoView;
                }

                //未来发布
                if (scheduledAt.getTime() > new Date().getTime()) {
                    taskService.createTaskPublishVideo(userId, text, videoFileInfo, videoFirstFrameInfo, "后台", scheduledAt);
                }

                //设为过去的日期并立即发布
                else {
                    statusService.createPost(userId, text, videoFileInfo, videoFirstFrameInfo, null, "后台", Post.ACL_PUBLIC, scheduledAt);
                }
            }

            //立即发布
            else {
                statusService.createPost(userId, text, videoFileInfo, videoFirstFrameInfo, null, "后台", Post.ACL_PUBLIC);
            }

            // 删除临时文件
            tempVideoFile.delete();
            tempPhotoFile.delete();

            redirectAttributes.asMap().clear(); //发布成功，不回填表单数据
            redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_SUCCESS_KEY, (isSchedule ? "发布视频成功(定时发布)" : "发布视频成功"));

            logger.debug("后台发布视频成功");
            return publisVideoView;

        } catch (Exception e) {

            logger.warn("后台发布视频失败", e);
            redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_ERROR_KEY, "发布视频失败，请重试");

            return publisVideoView;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object deletePublishVideoTask(@PathVariable(value = "id") String id) {
        taskService.deleteTaskPublishVideo(id);
        return ControllerUtils.AJAX_OK;
    }

}