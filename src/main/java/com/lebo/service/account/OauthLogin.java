package com.lebo.service.account;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;


/**
 * @author: Wei Liu
 * Date: 13-7-1
 * Time: PM3:06
 */
public abstract class OauthLogin {

    public void loginWithToken(String token) {
        shiroLogin(getShiroUser(token));
    }

    protected abstract ShiroUser getShiroUser(String token);

    private void shiroLogin(ShiroUser shiroUser) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.getPrincipal() != null) {
            currentUser.logout();
        }

        OauthToken authToken = new OauthToken("user", "user");
        authToken.setRememberMe(true);
        authToken.setShiroUser(shiroUser);

        try {
            currentUser.login(authToken);
        } catch (Exception e) {
            throw new RuntimeException("shiro 登录失败", e);
        }
    }

    public static class OauthToken extends UsernamePasswordToken {
        private ShiroUser shiroUser;

        public OauthToken(String username, String password) {
            super(username, password);
        }

        public ShiroUser getShiroUser() {
            return shiroUser;
        }

        public void setShiroUser(ShiroUser shiroUser) {
            this.shiroUser = shiroUser;
        }
    }
}
