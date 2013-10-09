package com.lebo.rest;

import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Wei Liu
 * Date: 13-10-9
 * Time: PM4:27
 */
@RequestMapping("/api/1/feedback")
@Controller
public class FeedbackRestController {

    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(value = "videoCanotPlay", method = RequestMethod.POST)
    @ResponseBody
    public Object videoCanotPlay(@RequestParam("objectType") String objectType,
                                 @RequestParam("objectId") String objectId,
                                 @RequestParam("videoUrl") String videoUrl,
                                 @RequestParam("client") String client) {

        feedbackService.addVideoConvertQueue(objectType, objectId);
        return ErrorDto.OK;
    }
}
