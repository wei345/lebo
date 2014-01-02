package com.lebo.web.admin;

import com.lebo.service.RobotService;
import com.lebo.web.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Collections3;

import java.util.List;

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

    private JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

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

    @RequestMapping(value = "group", method = RequestMethod.GET)
    public String group(Model model) {
        long beginTime = System.currentTimeMillis();

        List<RobotService.RobotGroup> groups = robotService.getGroups();

        model.addAttribute("list", groups);
        model.addAttribute("allGroupsJson", jsonMapper.toJson(
                Collections3.extractToList(groups, RobotService.RobotGroup.NAME_KEY)));
        model.addAttribute("spentSeconds", (System.currentTimeMillis() - beginTime) / 1000.0);

        return "admin/robot/group";
    }

    @RequestMapping(value = "renameGroup", method = RequestMethod.POST)
    @ResponseBody
    public Object renameGroup(@RequestParam("oldName") String oldName,
                              @RequestParam("newName") String newName) {

        robotService.renameGroup(oldName, newName);
        return ControllerUtils.AJAX_OK;
    }

    @RequestMapping(value = "deleteGroup", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteGroup(@RequestParam("name") String name) {

        robotService.deleteGroup(name);
        return ControllerUtils.AJAX_OK;
    }

    @RequestMapping(value = "updateGroup", method = RequestMethod.POST)
    @ResponseBody
    public Object updateGroup(@RequestParam("userId") String userId,
                              @RequestParam("groups") List<String> groups) {

        robotService.updateGroup(userId, groups);
        return ControllerUtils.AJAX_OK;
    }


    @RequestMapping(value = "comment", method = RequestMethod.GET)
    public String comment() {
        return "admin/robot/comment";
    }
}
