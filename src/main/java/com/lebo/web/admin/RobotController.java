package com.lebo.web.admin;

import com.lebo.service.RobotService;
import com.lebo.web.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Wei Liu
 * Date: 13-12-31
 * Time: PM3:00
 */
@Controller
@RequestMapping("/admin/robot")
public class RobotController {

    @Autowired
    private RobotService robotService;

    @RequestMapping(value = "comment", method = RequestMethod.GET)
    public String comment() {
        return "admin/robot/comment";
    }

    @RequestMapping(value = "set", method = RequestMethod.POST)
    @ResponseBody
    public Object set(@RequestParam("userId") String userId) {
        robotService.setRobot(userId);
        return ControllerUtils.AJAX_OK;
    }

    @RequestMapping(value = "unset", method = RequestMethod.POST)
    @ResponseBody
    public Object unset(@RequestParam("userId") String userId) {
        robotService.unsetRobot(userId);
        return ControllerUtils.AJAX_OK;
    }
}
