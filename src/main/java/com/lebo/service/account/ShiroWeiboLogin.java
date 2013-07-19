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
import java.util.Map;

/**
 * 微博OAuth登录
 *
 * @author: Wei Liu
 * Date: 13-7-16
 * Time: PM6:27
 */
@Service
public class ShiroWeiboLogin extends AbstractOAuthLogin {

    private Logger logger = LoggerFactory.getLogger(ShiroWeiboLogin.class);

    public static final String PROVIDER = "weibo";

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

    /*private String weibo_redirect_uri = "/weibo_callback";

    public String authorizeUrl(String baseurl) {
        return String.format("https://api.weibo.com/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s", weibo_api_key, baseurl + weibo_redirect_uri);
    }*/

    public ShiroUser getShiroUser(String token) {
        String uid = getUid(token);
        User user = accountService.findByOAuthId(oAuthId(PROVIDER, uid));

        // 第一次登录，创建用户
        if (user == null) {
            user = new User();
            Map userInfo = getUserInfo(token, uid);
            user.setScreenName(newScreenName((String) userInfo.get("screen_name")));
            user.setName((String) userInfo.get("name"));
            user.setProfileImageUrl((String) userInfo.get("profile_image_url"));
            user.setCreatedAt(new Date());
            LinkedHashSet<String> oAuthIds = new LinkedHashSet<String>(1);
            oAuthIds.add(oAuthId(PROVIDER, uid));
            user.setoAuthIds(oAuthIds);
            user.setLastSignInAt(user.getCreatedAt());
            user = accountService.saveUser(user, null);
        } else {
            accountService.updateLastSignInAt(user);
        }

        return new ShiroUser(user.getId(), user.getScreenName(), user.getName(), user.getProfileImageUrl(), PROVIDER, uid, token);
    }

    private String getUid(String token) {
        String url = String.format("https://api.weibo.com/2/account/get_uid.json?access_token=" + token);
        return restTemplate.getForObject(url, Map.class).get("uid").toString();
    }

    private Map getUserInfo(String token, String uid) {
        String url = String.format("https://api.weibo.com/2/users/show.json?uid=%s&access_token=%s", uid, token);
        Map userInfo = restTemplate.getForObject(url, Map.class);

        if (userInfo == null || userInfo.get("name") == null) {
            throw new RuntimeException("获取用户信息发生错误");
        }
        return userInfo;
    }
}


