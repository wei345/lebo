package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.FileStorageService;
import com.lebo.service.ServiceException;
import com.lebo.service.account.AccountService;
import com.lebo.service.account.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
                accountService.saveUserWithProfileImage(user, image.getInputStream());
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

    @RequestMapping(value = "updateApnsToken", method = RequestMethod.POST)
    @ResponseBody
    public Object updateApnsToken(@RequestParam(value = "apnsDevelopmentToken", required = false) String apnsDevelopmentToken,
                                  @RequestParam(value = "apnsProductionToken", required = false) String apnsProductionToken) {
        if (StringUtils.isBlank(apnsDevelopmentToken) && StringUtils.isBlank(apnsProductionToken)) {
            return ErrorDto.badRequest("参数apnsDevelopmentToken、apnsProductionToken不能都为空");
        }

        User user = accountService.getUser(accountService.getCurrentUserId());
        if (apnsDevelopmentToken != null) {
            user.setApnsDevelopmentToken(apnsDevelopmentToken);
        }
        if (apnsProductionToken != null) {
            user.setApnsProductionToken(apnsProductionToken);
        }
        accountService.saveUser(user);

        User dto = new User();
        dto.setRoles(null);
        dto.setId(user.getId());
        dto.setApnsDevelopmentToken(user.getApnsDevelopmentToken());
        dto.setApnsProductionToken(user.getApnsProductionToken());
        return dto;
    }

    /**
     * 更新Shiro中当前用户的用户名、图片.
     */
    private void updateCurrentUser(User user) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        shiroUser.screenName = user.getScreenName();
        shiroUser.profileImageUrl = user.getProfileImageUrl();
    }
}
