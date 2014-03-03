package com.lebo.web.admin;

import com.lebo.entity.Setting;
import com.lebo.entity.User;
import com.lebo.service.RobotService;
import com.lebo.service.account.AccountService;
import com.lebo.service.account.ShiroUser;
import com.lebo.service.param.PageRequest;
import com.lebo.service.param.PaginationParam;
import com.lebo.web.ControllerUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Collections3;

import java.io.IOException;
import java.util.List;

/**
 * 管理员管理用户的Controller.
 *
 * @author Wei Liu
 */
@Controller
@RequestMapping(value = "/admin/user")
public class UserAdminController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private RobotService robotService;
    public static JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    @RequestMapping(method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "robot", required = false) Boolean robot,
                       @RequestParam(value = "robotGroup", required = false) String robotGroup,
                       @RequestParam(value = "page", defaultValue = "0") int pageNo,
                       @RequestParam(value = "size", defaultValue = PaginationParam.DEFAULT_COUNT + "") int size,
                       @RequestParam(value = "orderBy", defaultValue = "_id") String orderBy,
                       @RequestParam(value = "order", defaultValue = "DESC") Sort.Direction order,
                       Model model) {

        long beginTime = System.currentTimeMillis();

        Page<User> page = accountService.adminSearchUser(
                q,
                userId,
                robot,
                robotGroup,
                new PageRequest(pageNo, size, new Sort(order, orderBy)));

        List<RobotService.RobotGroup> groups = robotService.getGroups();
        List<String> allGroups = Collections3.extractToList(groups, RobotService.RobotGroup.NAME_KEY);

        model.addAttribute("q", q);
        model.addAttribute("size", size);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("order", order.toString());

        model.addAttribute("users", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("allGroupsJson", jsonMapper.toJson(allGroups));
        model.addAttribute("allGroups", allGroups);
        model.addAttribute("spentSeconds", (System.currentTimeMillis() - beginTime) / 1000.0);

        return "admin/user/adminUserList";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("user", accountService.getUser(id));
        return "admin/user/adminUserForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@RequestParam(value = "id", required = false) String id,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "plainPassword", required = false) String plainPassword,
                         @RequestParam(value = "banned", required = false) Boolean banned,
                         RedirectAttributes redirectAttributes) {

        User user = accountService.getUser(id);

        if (StringUtils.isNotBlank(name) && !name.equals(user.getName())) {
            accountService.updateName(user.getId(), name);
            if (id.equals(accountService.getCurrentUserId())) {
                updateCurrentUserName(name);
            }
        }

        if (StringUtils.isNotBlank(plainPassword)) {
            accountService.updatePassword(user.getId(), plainPassword);
        }

        if (banned != null && banned != user.getBanned()) {
            accountService.updateBanned(user.getId(), banned);
        }

        redirectAttributes.addFlashAttribute("success", "更新 " + user.getScreenName() + " 成功");
        return "redirect:/admin/user";
    }

    @RequestMapping(value = "updateProfileImage/{id}", method = RequestMethod.GET)
    public String profileImageForm(@PathVariable(value = "id") String id, Model model) {
        model.addAttribute("user", accountService.getUser(id));
        return "admin/user/profileImageForm";
    }

    @RequestMapping(value = "updateProfileImage/{id}", method = RequestMethod.POST)
    public String updateProfileImage(@PathVariable(value = "id") String id,
                                     @RequestParam(value = "profileImage") MultipartFile profileImage,
                                     RedirectAttributes redirectAttributes) {

        User user = accountService.getUser(id);

        String userDetail = "redirect:/admin/user/update/" + id;

        if (user != null && profileImage != null && profileImage.getSize() > 0) {
            if (profileImage.getSize() > Setting.MAX_USER_PROFILE_IMAGE_LENGTH_BYTES) {
                redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_ERROR_KEY,
                        "图片大小不能超过 " + FileUtils.byteCountToDisplaySize(Setting.MAX_USER_PROFILE_IMAGE_LENGTH_BYTES));
                return userDetail;
            }

            try {
                accountService.updateProfileImage(user, profileImage.getInputStream());
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_ERROR_KEY,
                        NestedExceptionUtils.buildMessage("修改头像失败", e));
                return userDetail;
            }

            redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_SUCCESS_KEY, "修改头像成功");
            return userDetail;

        } else {
            redirectAttributes.addFlashAttribute(ControllerUtils.MODEL_ERROR_KEY, "用户或头像不存在");
            return userDetail;
        }
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getUser(@RequestParam(value = "id", required = false) String id, Model model) {
        if (id != null) {
            model.addAttribute("user", accountService.getUser(id));
        }
    }

    /**
     * 更新Shiro中当前用户的用户名.
     */
    private void updateCurrentUserName(String userName) {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        user.name = userName;
    }

}
