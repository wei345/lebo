package com.lebo.service.account;

import com.lebo.service.ServiceException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.hibernate.validator.constraints.NotBlank;
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
    private static ShiroDbLogin shiroDbLogin;

    public static OauthToken useOAuthLogin(String provider, String token, ServletContext application) {
        //新浪微博登录
        if (ShiroWeiboLogin.PROVIDER.equals(provider)) {
            if (shiroWeiboLogin == null) {
                shiroWeiboLogin = WebApplicationContextUtils.getWebApplicationContext(application).getBean(ShiroWeiboLogin.class);
            }
            shiroLogin.set(shiroWeiboLogin);

            return  new OauthToken(provider, token, true);
        }
        //不支持的登录类型
        else {
            throw new ServiceException(String.format("不支持的登录类型，provider : $s, token : %s", provider, token));
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

    public static AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
        return shiroLogin.get().doGetAuthorizationInfo(principals);
    }

    public static boolean credentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return shiroLogin.get().doCredentialsMatch(token, info);
    }

    protected abstract AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken, String realmName) throws AuthenticationException;

    protected abstract AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals);

    public abstract boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info);
}

class OauthToken extends UsernamePasswordToken {
    private String provider;
    private String grant;
    private String token;
    private String uid;

    public OauthToken() {
    }

    public OauthToken(String provider, String token, boolean rememberMe) {
        this.provider = provider;
        this.token = token;
        setRememberMe(rememberMe);
    }

    @NotBlank
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getGrant() {
        return grant;
    }

    public void setGrant(String grant) {
        this.grant = grant;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "OauthToken{" +
                "provider='" + provider + '\'' +
                ", grant='" + grant + '\'' +
                ", token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                "} " + super.toString();
    }
}
