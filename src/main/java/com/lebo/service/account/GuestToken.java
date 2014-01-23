package com.lebo.service.account;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * @author: Wei Liu
 * Date: 13-12-16
 * Time: PM7:01
 */
public class GuestToken implements AuthenticationToken, RememberMeAuthenticationToken {
    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public boolean isRememberMe() {
        return true;
    }
}
