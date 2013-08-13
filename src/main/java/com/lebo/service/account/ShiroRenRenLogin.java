package com.lebo.service.account;

import com.lebo.entity.User;
import com.mongodb.MongoException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 13-7-17
 * Time: PM3:49
 */
@Service
public class ShiroRenRenLogin extends AbstractOAuthLogin {
    private Logger logger = LoggerFactory.getLogger(ShiroWeiboLogin.class);
    public static String PROVIDER = "renren";

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken, String realmName) throws AuthenticationException {
        OauthToken authcToken = (OauthToken) authenticationToken;
        try {
            return new OAuthAuthenticationInfo(getShiroUser(authcToken.getToken()), realmName);
        } catch (MongoException e) {
            logger.warn("登录失败 - " + authcToken + " - " + e.getMessage(), e);
            return null;
        } catch (Exception e) {
            logger.info("登录失败 - " + authcToken + " - " + e.getMessage());
            return null;
        }
    }

    public ShiroUser getShiroUser(String token) {
        Map userInfo = (Map) getUserInfo(token).get("response");
        String uid = userInfo.get("id").toString();
        User user = accountService.findByOAuthId(oAuthId(PROVIDER, uid));

        // 第一次登录，创建用户
        if (user == null) {
            user = new User().initial();
            user.setScreenName(newScreenName((String) userInfo.get("name")));
            user.setName((String) userInfo.get("name"));

            List<Map<String, String>> avatar = (List) userInfo.get("avatar");
            String profileImageUrl = avatar.get(0).get("url");
            if (avatar.get(1) != null) {
                profileImageUrl = avatar.get(1).get("url");
            }
            user.setProfileImage(profileImageUrl);

            user.setCreatedAt(new Date());
            LinkedHashSet<String> oAuthIds = new LinkedHashSet<String>(1);
            oAuthIds.add(oAuthId(PROVIDER, uid));
            user.setoAuthIds(oAuthIds);
            user.setLastSignInAt(user.getCreatedAt());
            user = accountService.saveUser(user);
        } else {
            accountService.updateLastSignInAt(user);
        }

        ensureSaveProfileImage(user.getId(), user.getProfileImage());

        return new ShiroUser(user.getId(), user.getScreenName(), user.getName(), user.getProfileImageUrl(), PROVIDER, uid, token);
    }

    Map getUserInfo(String token) {
        String url = String.format("https://api.renren.com/v2/user/login/get?access_token=%s", token);
        Map userInfo = restTemplate.getForObject(url, Map.class);

        if (userInfo == null || ((Map) userInfo.get("response")).get("name") == null) {
            throw new RuntimeException("获取用户信息发生错误");
        }
        return userInfo;
    }
}
