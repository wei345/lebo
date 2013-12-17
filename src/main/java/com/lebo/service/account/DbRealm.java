package com.lebo.service.account;

import com.lebo.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;
import org.springside.modules.utils.Encodes;

/**
 * @author: Wei Liu
 * Date: 13-12-17
 * Time: AM11:51
 */
@Service
public class DbRealm extends AbstractRealm {

    public static final String PROVIDER = "local";

    @Override
    public boolean supports(AuthenticationToken token) {
        return (token instanceof UsernamePasswordToken);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken t = (UsernamePasswordToken) token;

        User user = getAccountService().findUserByEmail(t.getUsername());

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

                    getName());
        } else {
            return null;
        }
    }

    @Override
    public CredentialsMatcher getMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(AccountService.HASH_ALGORITHM);
        matcher.setHashIterations(AccountService.HASH_INTERATIONS);
        return matcher;
    }
}
