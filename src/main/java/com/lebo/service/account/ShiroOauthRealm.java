package com.lebo.service.account;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

public class ShiroOauthRealm extends AuthorizingRealm implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(ShiroOauthRealm.class);
    private WeiboOauth weiboLogin;
    private ApplicationContext applicationContext;

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        ensureDependencyInjection();

        OauthToken authcToken = (OauthToken) authenticationToken;
        try {
            if (WeiboOauth.PROVIDER.equals(authcToken.getProvider())) {
                ShiroUser shiroUser = weiboLogin.getShiroUser(authcToken.getToken());
                return new OauthAuthenticationInfo(shiroUser, getName());
            } else {
                //不支持的登录类型
                return null;
            }
        } catch (Exception e) {
            logger.info("登录失败 - " + authcToken + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //未来可能会有不同权限
//        ensureDependencyInjection();
//
//        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        return info;
        return null;
    }

    /**
     * 设定Password校验的Hash算法与迭代次数.
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        setCredentialsMatcher(new OauthCredentialsMatcher());
    }

    /**
     * shiro + spring-data-mongo，
     * 如果创建时注入MongoRepository(userDao)，会导致Spring初始化失败，因此将注入延迟到使用时。
     */
    private void ensureDependencyInjection() {
        if (weiboLogin == null) {
            weiboLogin = applicationContext.getBean(WeiboOauth.class);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static class OauthToken extends UsernamePasswordToken {
        private String provider;
        private String grant;
        private String token;
        private String uid;

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
}

class OauthAuthenticationInfo implements AuthenticationInfo {
    protected PrincipalCollection principals;

    public OauthAuthenticationInfo(Object principal, String realmName) {
        principals = new SimplePrincipalCollection(principal, realmName);
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return principals;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}

class OauthCredentialsMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return info != null && info.getPrincipals().getPrimaryPrincipal() != null;
    }
}


