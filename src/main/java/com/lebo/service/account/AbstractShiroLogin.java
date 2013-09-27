package com.lebo.service.account;

import com.lebo.service.ServiceException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * @author: Wei Liu
 * Date: 13-7-16
 * Time: PM6:01
 */
public abstract class AbstractShiroLogin {
    private static final ThreadLocal<AbstractShiroLogin> shiroLogin = new ThreadLocal<AbstractShiroLogin>();
    private static ShiroWeiboLogin shiroWeiboLogin;
    private static ShiroRenRenLogin shiroRenRenLogin;
    private static ShiroDbLogin shiroDbLogin;

    public static OauthToken useOAuthLogin(String provider, String token, ServletContext application) {
        //新浪微博登录
        if (ShiroWeiboLogin.PROVIDER.equals(provider)) {
            if (shiroWeiboLogin == null) {
                shiroWeiboLogin = WebApplicationContextUtils.getWebApplicationContext(application).getBean(ShiroWeiboLogin.class);
            }
            shiroLogin.set(shiroWeiboLogin);

            return new OauthToken(provider, token, true);
        }
        //人人网登录
        else if (ShiroRenRenLogin.PROVIDER.equals(provider)) {
            if (shiroRenRenLogin == null) {
                shiroRenRenLogin = WebApplicationContextUtils.getWebApplicationContext(application).getBean(ShiroRenRenLogin.class);
            }
            shiroLogin.set(shiroRenRenLogin);

            return new OauthToken(provider, token, true);
        }
        //不支持的登录类型
        else {
            throw new ServiceException(String.format("不支持的登录类型，provider : %s, token : %s", provider, token));
        }
    }

    public static UsernamePasswordToken useDbLogin(String username, String password, ServletContext application) {
        if (shiroDbLogin == null) {
            shiroDbLogin = WebApplicationContextUtils.getWebApplicationContext(application).getBean(ShiroDbLogin.class);
        }
        shiroLogin.set(shiroDbLogin);

        return new UsernamePasswordToken(username, password, true);
    }

    public static AuthenticationInfo getAuthenticationInfo(AuthenticationToken authenticationToken, String realmName) throws AuthenticationException {
        return shiroLogin.get().doGetAuthenticationInfo(authenticationToken, realmName);
    }

    public static boolean credentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return shiroLogin.get().doCredentialsMatch(token, info);
    }

    protected abstract AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken, String realmName) throws AuthenticationException;

    public abstract boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info);
}


