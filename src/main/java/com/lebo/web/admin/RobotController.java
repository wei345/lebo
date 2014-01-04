package com.lebo.web.admin;

import com.lebo.entity.RobotSaying;
import com.lebo.service.RobotService;
import com.lebo.service.param.PageRequest;
import com.lebo.web.ControllerUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Collections3;

import javax.validation.Valid;
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

    public static JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    //-- 机器人 --//

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

    //-- 机器人分组 --//

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

    //-- 机器人语库 --//
    @RequestMapping(value = "saying", method = RequestMethod.GET)
    public String sayingList(@Valid PageRequest pageRequest, Model model) {

        pageRequest.setSort(new Sort(Sort.Direction.ASC, RobotSaying.TEXT_KEY));

        model.addAttribute("page", robotService.getSayings(pageRequest));

        return "admin/robot/saying";
    }

    @RequestMapping(value = "saying/add", method = RequestMethod.GET)
    public String sayingAddForm() {
        return "admin/robot/sayingForm";
    }

    @RequestMapping(value = "saying/add", method = RequestMethod.POST)
    public String sayingAdd(RobotSaying robotSaying,
                            @RequestParam(value = "goToList", defaultValue = "true") boolean goToList,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if (robotService.getSayingByText(robotSaying.getText()) != null) {
            model.addAttribute("saying", robotSaying);
            model.addAttribute("tags", StringUtils.join(robotSaying.getTags(), ","));
            model.addAttribute("error", "该项已存在");
            return "admin/robot/sayingForm";
        }

        robotService.addSaying(robotSaying);
        redirectAttributes.addFlashAttribute("success", "添加成功");

        return goToList
                ?
                "redirect:/admin/robot/saying"
                :
                "redirect:/admin/robot/saying/add";
    }

    @RequestMapping(value = "saying/update/{id}", method = RequestMethod.GET)
    public String sayingUpdateForm(@PathVariable("id") String id,
                                   Model model) {

        RobotSaying robotSaying = robotService.getSaying(id);

        model.addAttribute("saying", robotSaying);
        model.addAttribute("tags", StringUtils.join(robotSaying.getTags(), ","));

        return "admin/robot/sayingForm";
    }

    @RequestMapping(value = "saying/update/{id}", method = RequestMethod.POST)
    public Object sayingUpdate(RobotSaying robotSaying) {
        robotService.updateSaying(robotSaying);
        return "redirect:/admin/robot/saying";
    }

    @RequestMapping(value = "saying/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object sayingDelete(@PathVariable("id") String id) {
        robotService.deleteSaying(id);
        return ControllerUtils.AJAX_OK;
    }

    //-- 机器人评论 --//

    @RequestMapping(value = "comment", method = RequestMethod.GET)
    public String comment() {
        return "admin/robot/comment";
    }
}
