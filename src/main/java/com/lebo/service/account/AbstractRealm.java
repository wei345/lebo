package com.lebo.service.account;

import com.google.common.eventbus.EventBus;
import com.lebo.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.PostConstruct;

public abstract class AbstractRealm extends AuthorizingRealm implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    /*
      @Autowired 会出现dao找不到，启动失败:

      The problem is that spring-data-mongodb requires a spring ApplicationEventMulticaster to have been initialised before it can be used.

      ShiroFilterFactoryBean is a beanPostProcessor, and as such, during initialisation,
      spring attempts to configure its realms(and hence my realm and spring data mongo based userDao).
      it fails because ApplicationEventMulticaster has not yet been created.

      见 org.springframework.context.support.AbstractApplicationContext.refresh
      见 http://stackoverflow.com/questions/10503101/spring-mongodb-and-apache-shiro
     */
    private AccountService accountService;

    private EventBus eventBus;

    @Autowired
    protected MongoTemplate mongoTemplate;

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        User user = getAccountService().getUser(shiroUser.getId());
        if (user == null) {//用户不存在，可能已被删除，退出登录
            SecurityUtils.getSubject().logout();
            return null;
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(user.getRoles());
        return info;
    }

    public AccountService getAccountService() {
        if (accountService == null) {
            accountService = applicationContext.getBean(AccountService.class);
        }

        return accountService;
    }

    public EventBus getEventBus() {
        if (eventBus == null) {
            eventBus = applicationContext.getBean(EventBus.class);
        }

        return eventBus;
    }

    public abstract boolean supports(AuthenticationToken token);

    public abstract CredentialsMatcher getMatcher();

    @PostConstruct
    public void setup() {
        setCredentialsMatcher(getMatcher());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}


