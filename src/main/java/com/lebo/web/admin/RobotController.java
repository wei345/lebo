package com.lebo.web.admin;

import com.lebo.entity.RobotSaying;
import com.lebo.entity.Setting;
import com.lebo.entity.Task;
import com.lebo.service.RobotService;
import com.lebo.service.SettingService;
import com.lebo.service.StatusService;
import com.lebo.service.TaskService;
import com.lebo.service.param.PageRequest;
import com.lebo.web.ControllerSetup;
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
import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private TaskService taskService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private SettingService settingService;

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

    //-- 机器人任务 --//
    @RequestMapping(value = "comment", method = RequestMethod.GET)
    public String comment(
            @RequestParam(value = "postId", required = false) String postId,
            Model model) {

        model.addAttribute("postId", postId);
        model.addAttribute("robots", robotService.getAllRobots());
        model.addAttribute("sayings", robotService.getAllSayings());
        model.addAttribute("robotGroups", robotService.getGroups());
        model.addAttribute("sayingTags", robotService.getAllSayingTags());

        return "admin/robot/comment";
    }

    @RequestMapping(value = "comment", method = RequestMethod.POST)
    public String comment(@RequestParam("comments") String commentsJson,
                          @RequestParam("postId") String postId,
                          RedirectAttributes redirectAttributes) throws ParseException {

        if (!statusService.postExists(postId)) {
            redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_ERROR_KEY, "该帖子不存在");
            return "redirect:/admin/robot/comment?postId=" + postId;
        }

        List<Map<String, String>> comments = jsonMapper.fromJson(commentsJson, jsonMapper.contructCollectionType(List.class, Map.class));

        for (Map<String, String> item : comments) {

            String taskData = jsonMapper.toJson(
                    new Task.RobotComment(
                            postId,
                            item.get("userId"),
                            item.get("text")));

            taskService.createTask(
                    "机器人评论",
                    Task.Type.ROBOT_COMMENT,
                    ControllerSetup.DEFAULT_DATE_FORMAT.parse(item.get("time")),
                    taskData);
        }

        redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_SUCCESS_KEY, "机器人评论提交成功");
        return "redirect:/admin/robot/comment";
    }

    @RequestMapping(value = "auto-favorite", method = RequestMethod.GET)
    public String autoFavorite(Model model) {
        model.addAttribute("robotTotalCount", robotService.countRobots());
        model.addAttribute("groups", robotService.getGroups());
        model.addAttribute("setting", settingService.getSetting());
        return "admin/robot/autoFavorite";
    }

    @RequestMapping(value = "auto-favorite", method = RequestMethod.POST)
    public String autoFavorite(@RequestParam(value = "robotAutoFavoriteEnable", defaultValue = "false") Boolean robotAutoFavoriteEnable,
                               @RequestParam(value = "robotAutoFavoriteRobotGroup") String robotAutoFavoriteRobotGroup,
                               @RequestParam(value = "robotAutoFavoriteRobotCountFrom") Integer robotAutoFavoriteRobotCountFrom,
                               @RequestParam(value = "robotAutoFavoriteRobotCountTo") Integer robotAutoFavoriteRobotCountTo,
                               @RequestParam(value = "robotAutoFavoriteTimeInMinuteFrom") Integer robotAutoFavoriteTimeInMinuteFrom,
                               @RequestParam(value = "robotAutoFavoriteTimeInMinuteTo") Integer robotAutoFavoriteTimeInMinuteTo,
                               RedirectAttributes redirectAttributes) {

        Setting setting = settingService.getSetting();
        setting.setRobotAutoFavoriteEnable(robotAutoFavoriteEnable);
        setting.setRobotAutoFavoriteRobotGroup(robotAutoFavoriteRobotGroup);
        setting.setRobotAutoFavoriteRobotCountFrom(robotAutoFavoriteRobotCountFrom);
        setting.setRobotAutoFavoriteRobotCountTo(robotAutoFavoriteRobotCountTo);
        setting.setRobotAutoFavoriteTimeInMinuteFrom(robotAutoFavoriteTimeInMinuteFrom);
        setting.setRobotAutoFavoriteTimeInMinuteTo(robotAutoFavoriteTimeInMinuteTo);
        settingService.saveSetting(setting);

        redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_SUCCESS_KEY, "保存成功");
        return "redirect:/admin/robot/auto-favorite";
    }

}
