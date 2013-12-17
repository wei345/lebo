package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.rest.dto.AccountSettingDto;
import com.lebo.rest.dto.CheckVersionDto;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.UserDto;
import com.lebo.service.account.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.BeanMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 13-7-1
 * Time: AM11:23
 */
@Controller
@RequestMapping(value = "/api")
public class HomeRestController {
    private Logger logger = LoggerFactory.getLogger(HomeRestController.class);

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "1/oauthLogin", method = RequestMethod.POST)
    @ResponseBody
    public Object oauthLogin(@RequestParam("provider") String provider,
                             @RequestParam(value = "uid", required = false) String uid,
                             @RequestParam("token") String token) {

        try {

            Subject subject = getSubject();

            //新浪微博登录
            if (WeiboToken.PROVIDER.equals(provider)) {

                subject.login(new WeiboToken(token));

                return getUserInfo();
            }

            //QQ登录
            if (QQToken.PROVIDER.equals(provider)) {

                if (StringUtils.isBlank(uid)) {
                    return ErrorDto.badRequest("uid不能为空");
                }

                subject.login(new QQToken(token, uid));

                return getUserInfo();
            }

            //人人网登录
            if (RenrenToken.PROVIDER.equals(provider)) {

                subject.login(new RenrenToken(token));

                return getUserInfo();
            }

            return ErrorDto.badRequest("不支持登录类型: " + provider);

        } catch (Exception e) {

            logger.info("登录失败", e);

            return ErrorDto.badRequest(e.getMessage());
        }
    }

    @RequestMapping(value = "1/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {

        try {

            Subject subject = getSubject();

            subject.login(new UsernamePasswordToken(username, password, true));

            return getUserInfo();

        } catch (Exception e) {

            logger.info("登录失败", e);

            return ErrorDto.badRequest(e.getMessage());
        }
    }

    @RequestMapping(value = "1/guestLogin", method = RequestMethod.POST)
    @ResponseBody
    public Object guestLogin() {

        try {

            Subject subject = getSubject();

            subject.login(new GuestToken());

            return getUserInfo();

        } catch (Exception e) {

            logger.info("登录失败", e);

            return ErrorDto.badRequest(e.getMessage());
        }
    }

    @RequestMapping(value = "1/logout", method = RequestMethod.POST)
    @ResponseBody
    public Object logout() {
        Subject subject = SecurityUtils.getSubject();
        ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();

        if (shiroUser != null) {
            User user = accountService.getUser(accountService.getCurrentUserId());
            logger.debug("退出登录 : 正在退出 {}({})", user.getScreenName(), user.getId());

            //注销设备
            logger.debug("退出登录 : 正在注销设备, apnsProductionToken : [{}], apnsDevelopmentToken : [{}]",
                    user.getApnsProductionToken(), user.getApnsDevelopmentToken());
            user.setApnsProductionToken("");
            user.setApnsDevelopmentToken("");
            accountService.updateUserSettings(user);

            //退出
            subject.logout();
            logger.debug("退出登录 : 已退出 {}({}) ", user.getScreenName(), user.getId());
            return accountService.toBasicUserDto(user);
        } else {
            return ErrorDto.unauthorized();
        }
    }

    /**
     * 返回客户端最新版本号，及告知客户端是否强制升级。
     */
    @RequestMapping(value = "1/checkVersion")
    @ResponseBody
    public Object checkVersion(@RequestParam("os") String os,  //android or ios
                               @RequestParam("osVersion") String osVersion,
                               @RequestParam("version") String version) {
        CheckVersionDto dto = new CheckVersionDto();

        dto.setForceUpgrade(false);
        /*TODO checkVersion 读取数据库返回真实版本号
        dto.setDownloadUrl("https://itunes.apple.com/cn/app/le-bo-6miao-shi-pin/id598266288?mt=8");
        dto.setReleaseAt(new Date());
        dto.setReleaseNotes(String.format("test %s %s %s", os, osVersion, version));
        dto.setVersion("1.2.3");*/

        return dto;
    }

    @RequestMapping(value = "**")
    @ResponseBody
    public Object notFound(HttpServletRequest request) {
        return ErrorDto.notFound(
                new StringBuilder("地址不存在: ")
                        .append(request.getMethod())
                        .append(" ")
                        .append(request.getRequestURL())
                        .toString());
    }

    /**
     * 用户登录成功后返回的用户信息
     */
    private Map<String, Object> getUserInfo() {

        String userId = accountService.getCurrentUserId();

        User user = accountService.getUser(userId);

        UserDto userDto = accountService.toBasicUserDto(user);

        AccountSettingDto settingDto = accountService.toAccountSettingDto(user);

        Map<String, Object> userInfo = new HashMap<String, Object>();

        BeanMapper.copy(userDto, userInfo);

        BeanMapper.copy(settingDto, userInfo);

        //去掉null值
        List<String> deletes = new ArrayList<String>();

        for (Map.Entry<String, Object> entry : userInfo.entrySet()) {
            if (entry.getValue() == null) {
                deletes.add(entry.getKey());
            }
        }

        for (String key : deletes) {
            userInfo.remove(key);
        }

        return userInfo;
    }

    /**
     * 返回待登录的Subject, 如果Subject已登录，则先退出，再返回
     */
    private Subject getSubject() {

        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() != null) {
            subject.logout();
        }

        return subject;
    }
}
