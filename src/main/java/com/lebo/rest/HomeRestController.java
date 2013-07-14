package com.lebo.rest;

import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.account.ShiroOauthRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * @author: Wei Liu
 * Date: 13-7-1
 * Time: AM11:23
 */
@Controller
@RequestMapping(value = "/api")
public class HomeRestController {
    private Logger logger = LoggerFactory.getLogger(HomeRestController.class);

    //TODO 可通过grant登录
    @RequestMapping(value = "1/oauthLogin", method = RequestMethod.POST)
    @ResponseBody
    public Object oauthLogin(@Valid ShiroOauthRealm.OauthToken oauthToken) {

        Subject currentUser = SecurityUtils.getSubject();

        // 登出当前用户
        if (currentUser.getPrincipal() != null) {
            currentUser.logout();
        }

        // 登录
        oauthToken.setRememberMe(true);
        try {
            currentUser.login(oauthToken);
            return SecurityUtils.getSubject().getPrincipal();
        } catch (Exception e) {
            logger.info("登录失败", e);
            return ErrorDto.BAD_REQUEST;
        }
    }

    @RequestMapping(value = "1/logout", method = RequestMethod.POST)
    @ResponseBody
    public Object logout() {
        Subject currentUser = SecurityUtils.getSubject();
        Object principal = currentUser.getPrincipal();
        if (principal != null) {
            currentUser.logout();
            return principal;
        } else {
            return ErrorDto.UNAUTHORIZED;
        }
    }

    @RequestMapping(value = "")
    @ResponseBody
    public Object index() {
        return "It works";
    }

    @RequestMapping(value = "**")
    @ResponseBody
    public Object notFound() {
        return ErrorDto.NOT_FOUND;
    }
}
