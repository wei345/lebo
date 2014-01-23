package com.lebo.service.account;

import com.lebo.entity.User;
import com.lebo.event.AfterUserLoginEvent;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author: Wei Liu
 * Date: 13-12-17
 * Time: PM12:28
 */
public abstract class AbstractOAuthRealm extends AbstractRealm {

    private Logger logger = LoggerFactory.getLogger(AbstractOAuthRealm.class);

    protected RestTemplate restTemplate = new RestTemplate();

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        try {

            AbstractOAuthToken abstractOAuthToken = (AbstractOAuthToken) token;

            User user = getUser(abstractOAuthToken);

            if (user.getBanned() != null && user.getBanned()) {
                logger.info("用户[" + user.getScreenName() + "]已被禁用");
                return null;
            }

            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                    new ShiroUser(user.getId(),
                            user.getScreenName(),
                            user.getName(),
                            user.getProfileImageUrl(),
                            abstractOAuthToken.getProvider()),
                    null,
                    getName());

            getEventBus().post(new AfterUserLoginEvent(user));

            return info;

        } catch (Exception e) {

            logger.info("登录失败, token: " + token, e);

            return null;
        }
    }

    @Override
    public CredentialsMatcher getMatcher() {
        return new CredentialsMatcher() {
            @Override
            public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
                return info != null && info.getPrincipals().getPrimaryPrincipal() != null;
            }
        };
    }

    protected abstract User getUser(AbstractOAuthToken token);

    protected String getGender(String gender) {

        if ("女".equals(gender) || "FEMALE".equalsIgnoreCase(gender) || "f".equalsIgnoreCase(gender)) {
            return User.GENDER_FEMALE;
        }

        if ("男".equals(gender) || "MALE".equalsIgnoreCase(gender) || "m".equalsIgnoreCase(gender)) {
            return User.GENDER_MALE;
        }

        return null;
    }

    public static String oAuthId(String provider, String uid) {
        return provider + "/" + uid;
    }
}
