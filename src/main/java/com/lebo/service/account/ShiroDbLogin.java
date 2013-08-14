package com.lebo.service.account;

import com.lebo.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.utils.Encodes;

import javax.annotation.PostConstruct;

/**
 * @author: Wei Liu
 * Date: 13-7-16
 * Time: PM4:31
 */
@Service
public class ShiroDbLogin extends AbstractShiroLogin {

    public static final String PROVIDER = "local";

    @Autowired
    private AccountService accountService;

    private CredentialsMatcher credentialsMatcher;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken, String realmName) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        User user = accountService.findUserByEmail(token.getUsername());
        if (user != null) {
            byte[] salt = Encodes.decodeHex(user.getSalt());
            return new SimpleAuthenticationInfo(
                    new ShiroUser(user.getId(),
                            user.getScreenName(),
                            user.getName(),
                            user.getProfileImageUrl(),
                            PROVIDER),
                    user.getPassword(),
                    ByteSource.Util.bytes(salt),
                    realmName);
        } else {
            return null;
        }
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return credentialsMatcher.doCredentialsMatch(token, info);
    }

    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(AccountService.HASH_ALGORITHM);
        matcher.setHashIterations(AccountService.HASH_INTERATIONS);
        credentialsMatcher = matcher;
    }
}
