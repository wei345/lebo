package com.lebo.web.account;

import com.lebo.entity.User;
import com.lebo.service.account.AccountService;
import com.lebo.service.account.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户修改自己资料的Controller.
 *
 * @author Wei Liu
 */
@Controller
@RequestMapping(value = "/profile")
public class ProfileController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String updateForm(Model model) {
        String id = accountService.getCurrentUserId();
        model.addAttribute("user", accountService.getUser(id));
        return "account/profile";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String update(@RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "email", required = false) String email,
                         @RequestParam(value = "screenName", required = false) String screenName,
                         @RequestParam(value = "plainPassword", required = false) String plainPassword) {

        User user = accountService.getUser(accountService.getCurrentUserId());

        if (StringUtils.isNotBlank(name) && !name.equals(user.getName())) {
            accountService.updateName(user.getId(), name);
            updateCurrentUserName(name);
        }

        if (StringUtils.isNotBlank(email) && !email.equals(user.getEmail())) {
            accountService.updateEmail(user.getId(), email);
        }

        if (StringUtils.isNotBlank(screenName) && !screenName.equals(user.getScreenName())) {
            accountService.updateScreenName(user.getId(), screenName);
        }

        if (StringUtils.isNotBlank(plainPassword)) {
            accountService.updatePassword(user.getId(), plainPassword);
        }

        return "redirect:/";
    }

    /**
     * 更新Shiro中当前用户的用户名.
     */
    private void updateCurrentUserName(String userName) {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        user.name = userName;
    }
}
