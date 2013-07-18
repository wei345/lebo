package com.lebo.service.account;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.PostConstruct;

public class ShiroRealm extends AuthorizingRealm {

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return AbstractShiroLogin.getAuthenticationInfo(authenticationToken, getName());
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return AbstractShiroLogin.getAuthorizationInfo(principals);
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        setCredentialsMatcher(new CredentialsMatcher() {
            @Override
            public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
                return AbstractShiroLogin.credentialsMatch(token, info);
            }
        });
    }
}


