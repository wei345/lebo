package com.lebo.rest;

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
    public Object update(@RequestParam(value = "media") MultipartFile media, @RequestParam(value = "text") String text) {
        try {

            return statusService.update(ControllerUtils.getCurrentUserId(), text, media.getInputStream(), media.getSize(), media.getName());

        } catch (Exception e) {
            logger.info("发布Tweet失败", e);
            return new ErrorDto(e.getMessage());
        }

    }
}
