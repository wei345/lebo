package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.account.AccountService;
import com.lebo.service.account.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: Wei Liu
 * Date: 13-7-22
 * Time: PM9:03
 */
@Controller
@RequestMapping("/api/1/account")
public class AccountRestController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "updateProfile", method = RequestMethod.POST)
    @ResponseBody
    public Object updateProfile(@RequestParam(value = "screenName", required = false) String screenName,
                                @RequestParam(value = "image", required = false) MultipartFile image,
                                @RequestParam(value = "description", required = false) String description) {
        User user = accountService.getUser(accountService.getCurrentUserId());

        if (StringUtils.isNotBlank(screenName)) {
            if (!accountService.isScreenNameAvailable(screenName, accountService.getCurrentUserId())) {
                return ErrorDto.badRequest(String.format("%s 已被占用", screenName));
            }
            user.setScreenName(screenName);
        }

        if (StringUtils.isNotBlank(description)) {
            user.setDescription(description);
        }

        if (image != null && image.getSize() > 0) {
            try {
                accountService.updateUserWithProfileImage(user, image.getInputStream());
            } catch (IOException e) {
                return ErrorDto.badRequest(NestedExceptionUtils.buildMessage("更新用户失败", e));
            }
        } else {
            accountService.saveUser(user);
        }

        //更新ShiroUser
        updateCurrentUser(user);
        return accountService.toUserDto(user);
    }

    @RequestMapping(value = "checkScreenName", method = RequestMethod.POST)
    @ResponseBody
    public Object checkScreenName(@RequestParam(value = "screenName") String screenName) {
        if (StringUtils.isBlank(screenName)) {
            return ErrorDto.badRequest("参数screenName不能为空");
        }

        if (accountService.isScreenNameAvailable(screenName, accountService.getCurrentUserId())) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * 更新当前用户账号设置，返回当前用户账号设置。
     */
    @RequestMapping(value = "settings", method = RequestMethod.POST)
    @ResponseBody
    public Object updateSettings(@ModelAttribute("userSettings") User user) {
        accountService.updateUserSettings(user);
        return accountService.toAccountSettingDto(user);
    }

    /**
     * 返回当前登录账号的设置。
     */
    @RequestMapping(value = "settings", method = RequestMethod.GET)
    @ResponseBody
    public Object getSettings() {
        User user = accountService.getUserSettings(accountService.getCurrentUserId());
        return accountService.toAccountSettingDto(user);
    }

    /**
     * 更新Shiro中当前用户的用户名、图片.
     */
    private void updateCurrentUser(User user) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        shiroUser.screenName = user.getScreenName();
        shiroUser.profileImageUrl = user.getProfileImageUrl();
    }

    @ModelAttribute
    public void getUser(Model model) {
        User user = accountService.getUserSettings(accountService.getCurrentUserId());
        model.addAttribute("userSettings", user);
    }
}
