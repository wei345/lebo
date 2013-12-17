package com.lebo.service.account;

import com.lebo.entity.User;
import com.lebo.service.SettingService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Service;

/**
 * @author: Wei Liu
 * Date: 13-12-16
 * Time: PM7:05
 */
@Service
public class GuestRealm extends AbstractRealm {

    private SettingService settingService;

    public static final String PROVIDER = "local";

    @Override
    public boolean supports(AuthenticationToken token) {
        return (token instanceof GuestToken);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String guestId = getSettingService().getSetting().getGuestAccountId();

        if (guestId == null) {
            return null;
        }

        User user = getAccountService().getUser(guestId);

        if (user == null) {
            return null;
        }

        return new SimpleAuthenticationInfo(
                new ShiroUser(user.getId(),
                        user.getScreenName(),
                        user.getName(),
                        user.getProfileImageUrl(),
                        PROVIDER),
                null,
                getName());

    }

    @Override
    public CredentialsMatcher getMatcher() {
        return new CredentialsMatcher() {
            @Override
            public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
                return (token instanceof GuestToken
                        && info != null
                        && info.getPrincipals().getPrimaryPrincipal() != null);
            }
        };
    }

    public SettingService getSettingService() {
        if (settingService == null) {
            settingService = applicationContext.getBean(SettingService.class);
        }

        return settingService;
    }
}
