package com.lebo.rest;

import com.lebo.entity.Setting;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.DuplicateException;
import com.lebo.service.MessageService;
import com.lebo.service.account.AccountService;
import com.lebo.web.ControllerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 非公开、用户之间的消息。相互关注才能发私信。
 *
 * @author: Wei Liu
 * Date: 13-7-14
 * Time: PM7:52
 */
@Controller
@RequestMapping("/api/1/directMessages")
public class DirectMessageRestController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    private Logger logger = LoggerFactory.getLogger(DirectMessageRestController.class);

    @RequestMapping(value = "new", method = RequestMethod.POST)
    @ResponseBody
    public Object newMessage(@RequestParam(value = "userId", required = false) String userId,
                             @RequestParam(value = "screenName", required = false) String screenName,
                             @RequestParam(value = "video", required = false) MultipartFile video,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             @RequestParam(value = "text", required = false) String text,
                             @RequestParam(value = "source", required = false) String source) {
        try {
            if (StringUtils.isBlank(text) && !(video != null && image != null)) {
                return ErrorDto.badRequest("参数错误");
            }

            userId = accountService.getUserId(userId, screenName);

            if (video != null && image != null) {
                if (video.getSize() > Setting.MAX_VIDEO_LENGTH_BYTES || image.getSize() > Setting.MAX_IMAGE_LENGTH_BYTES) {
                    return ErrorDto.badRequest("上传的视频或图片太大");
                }
            }

            return messageService.newMessage(accountService.getCurrentUserId(), userId, text,
                    ControllerUtils.getFileInfo(video), ControllerUtils.getFileInfo(image), source);

        } catch (DuplicateException e) {
            return ErrorDto.duplicate();
        } catch (Exception e) {
            logger.info("发送私信失败", e);
            return ErrorDto.badRequest(e.getMessage());
        }
    }

}
