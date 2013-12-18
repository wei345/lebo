package com.lebo.service.account;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.realm.Realm;

/**
 * @author: Wei Liu
 * Date: 13-12-16
 * Time: PM6:13
 */
public class AuthcStrategy extends AbstractAuthenticationStrategy {

    @Override
    public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {

        if (realm instanceof QQRealm) {

        }


        return super.beforeAttempt(realm, token, aggregate);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
        return super.afterAttempt(realm, token, singleRealmInfo, aggregateInfo, t);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public AuthenticationInfo afterAllAttempts(AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        return super.afterAllAttempts(token, aggregate);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
