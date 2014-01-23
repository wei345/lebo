package com.lebo.service.account;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * @author: Wei Liu
 * Date: 13-12-17
 * Time: PM6:17
 */
public abstract class AbstractOAuthToken implements AuthenticationToken, RememberMeAuthenticationToken {
    abstract String getProvider();

    @Override
    public boolean isRememberMe() {
        return true;
    }
}
