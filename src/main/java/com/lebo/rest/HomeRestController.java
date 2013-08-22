package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.account.AbstractShiroLogin;
import com.lebo.service.account.AccountService;
import com.lebo.service.account.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;

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
    private ServletContext context;
    @Autowired
    private AccountService accountService;

    //TODO 可通过grant登录
    @RequestMapping(value = "1/oauthLogin", method = RequestMethod.POST)
    @ResponseBody
    public Object oauthLogin(@RequestParam("provider") String provider,
                             @RequestParam("token") String token) {

        // 登出当前用户
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.getPrincipal() != null) {
            currentUser.logout();
        }

        // 登录
        try {
            currentUser.login(AbstractShiroLogin.useOAuthLogin(provider, token, context));
            return accountService.toBasicUserDto(accountService.getUser(accountService.getCurrentUserId()));
        } catch (Exception e) {
            logger.info("登录失败", e);
            return ErrorDto.badRequest(e.getMessage());
        }
    }

    @RequestMapping(value = "1/logout", method = RequestMethod.POST)
    @ResponseBody
    public Object logout() {
        Subject currentUser = SecurityUtils.getSubject();
        ShiroUser principal = (ShiroUser) currentUser.getPrincipal();
        if (principal != null) {
            currentUser.logout();
            return accountService.toBasicUserDto(accountService.getUser(principal.getId()));
        } else {
            return ErrorDto.unauthorized();
        }
    }

    @RequestMapping(value = "1/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        // 登出当前用户
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.getPrincipal() != null) {
            currentUser.logout();
        }

        // 登录
        try {
            currentUser.login(AbstractShiroLogin.useDbLogin(username, password, context));
            return SecurityUtils.getSubject().getPrincipal();
        } catch (Exception e) {
            logger.info("登录失败", e);
            return ErrorDto.badRequest(e.getMessage());
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
        return ErrorDto.notFound();
    }
}
