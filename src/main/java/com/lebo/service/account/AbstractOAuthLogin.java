package com.lebo.service.account;

import com.lebo.entity.User;
import com.lebo.event.AfterUserLoginEvent;
import com.lebo.event.ApplicationEventBus;
import com.lebo.jms.ProfileImageMessageProducer;
import com.lebo.service.FileStorageService;
import com.mongodb.MongoException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-7-17
 * Time: PM5:31
 */
public abstract class AbstractOAuthLogin extends AbstractShiroLogin {
    protected final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    protected AccountService accountService;
    @Autowired
    protected FileStorageService fileStorageService;
    @Autowired
    protected ProfileImageMessageProducer profileImageMessageProducer;
    @Autowired
    protected ApplicationEventBus eventBus;

    private Logger logger = LoggerFactory.getLogger(AbstractOAuthLogin.class);

    public static String oAuthId(String provider, String uid) {
        return provider + "/" + uid;
    }

    /**
     * 返回一个未被占用、唯一的screenName，以给定字符串作为前缀。
     */
    protected String newScreenName(String prefix) {
        User user;
        String name = prefix;
        do {
            user = accountService.findUserByScreenName(name);
            if (user == null) {
                break;
            }
            name = prefix + new Date().hashCode();
        } while (true);

        return name;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return info != null && info.getPrincipals().getPrimaryPrincipal() != null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken, String realmName) throws AuthenticationException {
        OauthToken authcToken = (OauthToken) authenticationToken;
        try {
            User user = getUser(authcToken.getToken());
            OAuthAuthenticationInfo info = new OAuthAuthenticationInfo(
                    new ShiroUser(user.getId(), user.getScreenName(), user.getName(), user.getProfileImageUrl(), getPrivoder()),
                    realmName);

            eventBus.post(new AfterUserLoginEvent(user));

            return info;
        } catch (MongoException e) {
            logger.warn("登录失败, token: " + authcToken, e);
            return null;
        } catch (Exception e) {
            logger.info("登录失败, token: " + authcToken, e);
            return null;
        }
    }

    protected Character getGender(String gender){

        if("女".equals(gender) || "FEMALE".equalsIgnoreCase(gender) || "f".equalsIgnoreCase(gender)){
            return User.GENDER_FEMALE;
        }

        if("男".equals(gender) || "MALE".equalsIgnoreCase(gender) || "m".equalsIgnoreCase(gender)){
            return User.GENDER_MALE;
        }

        return null;
    }

    /**
     * 根据OAuth Token获取用户信息。
     *
     * @param token OAuth Token
     * @return 返回ShiroUser，不可返回null，如果OAuth验证失败，要抛出异常
     */
    abstract public User getUser(String token);

    abstract public String getPrivoder();
}

class OAuthAuthenticationInfo implements AuthenticationInfo {
    protected PrincipalCollection principals;

    public OAuthAuthenticationInfo(Object principal, String realmName) {
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
