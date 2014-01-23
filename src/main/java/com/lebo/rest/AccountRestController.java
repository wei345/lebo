package com.lebo.rest;

import com.lebo.entity.FileInfo;
import com.lebo.entity.Setting;
import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.ALiYunStorageService;
import com.lebo.service.account.AccountService;
import com.lebo.service.account.ShiroUser;
import com.lebo.util.ContentTypeMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class AccountRestController {

    private static final String API_1_ACCOUNT = "/api/1/account/";
    private static final String API_1_1_ACCOUNT = "/api/1.1/account/";

    @Autowired
    private AccountService accountService;
    @Autowired
    private ALiYunStorageService aLiYunStorageService;

    private Logger logger = LoggerFactory.getLogger(AccountRestController.class);

    @RequestMapping(value = API_1_ACCOUNT + "updateProfile", method = RequestMethod.POST)
    @ResponseBody
    public Object updateProfile(@RequestParam(value = "screenName", required = false) String screenName,
                                @RequestParam(value = "image", required = false) MultipartFile image,
                                @RequestParam(value = "description", required = false) String description) {
        User user = accountService.getUser(accountService.getCurrentUserId());

        if (StringUtils.isNotBlank(screenName)) {
            user.setScreenName(screenName);
            accountService.updateScreenName(user.getId(), screenName);
        }

        if (StringUtils.isNotBlank(description)) {
            user.setDescription(description);
            accountService.updateDescription(user.getId(), description);
        }

        if (image != null && image.getSize() > 0) {
            try {
                accountService.updateProfileImage(user, image.getInputStream());
            } catch (IOException e) {
                return ErrorDto.badRequest(NestedExceptionUtils.buildMessage("更新用户失败", e));
            }
        }

        //更新ShiroUser
        updateCurrentUser(user);
        return accountService.toUserDto(user);
    }

    @RequestMapping(value = API_1_ACCOUNT + "checkScreenName", method = RequestMethod.POST)
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
    @RequestMapping(value = API_1_ACCOUNT + "settings", method = RequestMethod.POST)
    @ResponseBody
    public Object updateSettings(@ModelAttribute("userSettings") User user) {
        accountService.updateUserSettings(user);
        return accountService.toAccountSettingDto(user);
    }

    /**
     * 返回当前登录账号的设置。
     */
    @RequestMapping(value = API_1_ACCOUNT + "settings", method = RequestMethod.GET)
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

    //-- v1.1 --//

    @RequestMapping(value = API_1_1_ACCOUNT + "updateProfileWithMedia.json", method = RequestMethod.POST)
    @ResponseBody
    public Object updateProfileWithMedia_v1_1(@RequestParam(value = "screenName", required = false) String screenName,
                                              @RequestParam(value = "imageUrl", required = false) String imageUrl,
                                              @RequestParam(value = "description", required = false) String description) {

        User user = accountService.getUser(accountService.getCurrentUserId());

        if (StringUtils.isNotBlank(screenName)) {
            user.setScreenName(screenName);
            accountService.updateScreenName(user.getId(), screenName);
        }

        if (StringUtils.isNotBlank(description)) {
            user.setDescription(description);
            accountService.updateDescription(user.getId(), description);
        }

        FileInfo image = aLiYunStorageService.get(aLiYunStorageService.getKeyFromUrl(imageUrl));

        if (image != null && image.getLength() > 0) {
            if (image.getLength() > Setting.MAX_USER_PROFILE_IMAGE_LENGTH_BYTES) {
                return ErrorDto.badRequest("图片大小不能超过 " + FileUtils.byteCountToDisplaySize(Setting.MAX_USER_PROFILE_IMAGE_LENGTH_BYTES));
            }

            try {
                accountService.updateProfileImage(user, image.getContent());
            } catch (IOException e) {
                return ErrorDto.badRequest(NestedExceptionUtils.buildMessage("更新用户失败", e));
            }
        }

        //更新ShiroUser
        updateCurrentUser(user);
        return accountService.toUserDto(user);
    }

    @RequestMapping(value = API_1_1_ACCOUNT + "updateProfileBackgroundImage.json", method = RequestMethod.POST)
    @ResponseBody
    public Object updateProfileBackgroundImage_v1_1(@RequestParam(value = "imageUrl") String imageUrl) {
        String userId = accountService.getCurrentUserId();

        //读取图片
        FileInfo imageFileInfo = aLiYunStorageService.get(aLiYunStorageService.getKeyFromUrl(imageUrl));

        if (imageFileInfo == null) {
            return ErrorDto.badRequest("图片不存在 url : " + imageUrl);
        }

        //检查图片类型
        if (!ContentTypeMap.IMAGE_JPEG.equals(imageFileInfo.getContentType())
                && !ContentTypeMap.IMAGE_PNG.equals(imageFileInfo.getContentType())) {
            return ErrorDto.badRequest("不允许文件类型 " + imageFileInfo.getContentType());
        }

        //检查图片大小
        if (imageFileInfo.getLength() > Setting.MAX_USER_PROFILE_BACKGROUND_IMAGE_LENGTH_BYTES) {
            return ErrorDto.badRequest("图片大小不能超过 " +
                    FileUtils.byteCountToDisplaySize(Setting.MAX_USER_PROFILE_BACKGROUND_IMAGE_LENGTH_BYTES));
        }

        //保存图片
        try {

            User user = accountService.updateProfileBackgroundImage(userId, imageFileInfo.getContent());
            return accountService.toBasicUserDto(user);

        } catch (IOException e) {

            logger.debug("保存用户背景图片失败", e);
            return ErrorDto.badRequest("保存用户背景图片失败: " + e.getMessage());

        } finally {

            aLiYunStorageService.delete(imageFileInfo.getKey());

        }
    }
}
