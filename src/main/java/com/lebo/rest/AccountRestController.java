package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.GridFsService;
import com.lebo.service.ServiceException;
import com.lebo.service.account.AccountService;
import com.lebo.service.account.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private GridFsService gridFsService;

    @RequestMapping(value = "updateProfile", method = RequestMethod.POST)
    @ResponseBody
    public Object updateProfile(@RequestParam(value = "screenName", required = false) String screenName,
                                @RequestParam(value = "image", required = false) MultipartFile image,
                                @RequestParam(value = "description", required = false) String description) {
        String userId = accountService.getCurrentUserId();
        User user = accountService.getUser(userId);

        if (StringUtils.isNotBlank(screenName)) {
            if (!accountService.isScreenNameAvailable(screenName, accountService.getCurrentUserId())) {
                return ErrorDto.badRequest(String.format("%s 已被占用", screenName));
            }
            user.setScreenName(screenName);
        }

        String oldImageId = null;
        if (image != null && image.getSize() > 0) {
            try {
                String imageId = gridFsService.save(image.getInputStream(), image.getOriginalFilename(), image.getContentType());
                oldImageId = user.getProfileImageUrl();
                user.setProfileImageUrl(imageId);
            } catch (IOException e) {
                return ErrorDto.badRequest(new ServiceException("保存图片失败", e).getMessage());
            }
        }
        if (StringUtils.isNotBlank(description)) {
            user.setDescription(description);
        }
        accountService.saveUser(user);

        //删除旧图片
        if (accountService.isMongoId(oldImageId)) {
            gridFsService.delete(oldImageId);
        }
        //更新ShiroUser
        if (oldImageId != null) {
            updateCurrentUser(user);
        }
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
     * 更新Shiro中当前用户的用户名、图片.
     */
    private void updateCurrentUser(User user) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        shiroUser.screenName = user.getScreenName();
        shiroUser.profileImageUrl = gridFsService.getContentUrl(user.getProfileImageUrl(), null);
    }
}
