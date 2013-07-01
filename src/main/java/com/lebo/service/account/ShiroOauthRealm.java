package com.lebo.service.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.lebo.entity.OpenUser;
import com.lebo.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springside.modules.utils.Encodes;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Map;

public class ShiroOauthRealm extends AuthorizingRealm implements ApplicationContextAware {

    protected AccountService accountService;
    private ApplicationContext applicationContext;

    private ByteSource credentialsSalt = ByteSource.Util.bytes(Encodes.decodeHex("6d65d24122c30500"));
    private String hashedCredentials = "2488aa0c31c624687bd9928e0a5d29e7d1ed520b";

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        ensureAccountService();

        OauthLogin.OauthToken authcToken = (OauthLogin.OauthToken) authenticationToken;

        Assert.notNull(authcToken.getShiroUser(), "Shiro user can not be null");

        return new SimpleAuthenticationInfo(authcToken.getShiroUser(),
                hashedCredentials, credentialsSalt, getName());
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ensureAccountService();

        //ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        // 目录没有角色
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(AccountService.HASH_ALGORITHM);
        matcher.setHashIterations(AccountService.HASH_INTERATIONS);

        setCredentialsMatcher(matcher);
    }

    private void ensureAccountService() {
        if (accountService == null) {
            accountService = applicationContext.getBean(AccountService.class);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
