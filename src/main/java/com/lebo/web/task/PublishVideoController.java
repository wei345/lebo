package com.lebo.web.task;

import com.lebo.entity.FileInfo;
import com.lebo.entity.Task;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "/admin/tasks/publish-video")
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
    public String publishVideoForm() {
        return "task/publishVideo";
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
        String view = "redirect:/admin/tasks/publish-video";

        boolean isSchedule = StringUtils.isNotBlank(publishDate);

        //发生错误时回填表单数据
        redirectAttributes.addFlashAttribute("screenName", screenName);
        redirectAttributes.addFlashAttribute("text", text);
        redirectAttributes.addFlashAttribute("publishDate", publishDate);
        redirectAttributes.addFlashAttribute("publishTime", publishTime);

        //检查视频格式
        if (!TaskService.UPLOAD_VIDEO_EXT.equals(FilenameUtils.getExtension(video.getOriginalFilename()))) {
            redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_ERROR_KEY, String.format("视频文件格式错误，只允许 %s", TaskService.UPLOAD_VIDEO_EXT));
            return view;
        }

        //查找用户ID
        String userId;
        try {
            userId = accountService.getUserId(null, screenName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_ERROR_KEY, String.format("失败，找不到用户[%s]", screenName));
            return view;
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

            //定时发布
            if (isSchedule) {
                Date scheduledAt;
                try {
                    scheduledAt = scheduleDateFormat.parse(String.format("%s %s", publishDate, publishTime));
                } catch (ParseException ex) {
                    redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_ERROR_KEY, String.format("日期格式错误，正确格式为: %s", scheduleDateFormat.toPattern()));
                    return view;
                }

                taskService.createTaskPublishVideo(userId, text, videoFileInfo, videoFirstFrameInfo, "后台", scheduledAt);
            }
            //立即发布
            else {
                statusService.createPost(userId, text, videoFileInfo, videoFirstFrameInfo, null, "后台");
            }

            // 删除临时文件
            tempVideoFile.delete();
            tempPhotoFile.delete();

            redirectAttributes.asMap().clear(); //发布成功，不回填表单数据
            redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_SUCCESS_KEY, (isSchedule ? "发布视频成功(定时发布)" : "发布视频成功"));
            logger.debug("后台发布视频成功");
            return view;
        } catch (Exception e) {
            logger.warn("后台发布视频失败", e);
            redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_ERROR_KEY, "发布视频失败，请重试");
            return view;
        }
    }

}