package com.lebo.rest;

import com.lebo.service.DuplicateException;
import com.lebo.service.status.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-3
 * Time: PM8:35
 */
@Controller
@RequestMapping(value = "/api/v1/statuses")
public class StatusRestController {
    @Autowired
    private StatusService statusService;

    private Logger logger = LoggerFactory.getLogger(StatusRestController.class);

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@RequestParam(value = "video") MultipartFile video,
                         @RequestParam(value = "image") MultipartFile image,
                         @RequestParam(value = "text") String text) {
        try {

            List<StatusService.File> files = Arrays.asList(
                    new StatusService.File(video.getInputStream(), video.getOriginalFilename(), video.getContentType()),
                    new StatusService.File(image.getInputStream(), image.getOriginalFilename(), image.getContentType())
            );

            return statusService.update(ControllerUtils.getCurrentUserId(), text, files);

        } catch (DuplicateException e) {
            return ErrorDto.DUPLICATE;
        } catch (Exception e) {
            logger.info("发布Tweet失败", e);
            return new ErrorDto(e.getMessage());
        }

    }
}
