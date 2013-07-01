package com.lebo.rest;

import com.lebo.service.account.WeiboLogin;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Wei Liu
 * Date: 13-6-27
 * Time: PM6:02
 */
@Controller
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginRestController {

    private Logger logger = LoggerFactory.getLogger(LoginRestController.class);

    @Autowired
    private WeiboLogin weiboLogin;

    @RequestMapping(value="login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestParam("provider") String provider, @RequestParam("uid") String uid, @RequestParam("token") String token) {
        if(WeiboLogin.PROVIDER.equals(provider)){
            try{
                weiboLogin.loginWithToken(token);
                return SecurityUtils.getSubject().getPrincipal();
            }catch (Exception e){
                logger.warn("登录失败", e);
                return ErrorDto.BAD_REQUEST;
            }
        }else{ // 不支持的登录类型
            return ErrorDto.BAD_REQUEST;
        }
    }

    @RequestMapping(value="logout", method = RequestMethod.POST)
    @ResponseBody
    public Object logout(){
        Subject currentUser = SecurityUtils.getSubject();
        Object principal = currentUser.getPrincipal();
        if (principal != null) {
            currentUser.logout();
            return principal;
        }else{
            return ErrorDto.UNAUTHORIZED;
        }
    }

}
