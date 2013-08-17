package com.lebo.service.account;

import com.lebo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    /*private String weibo_redirect_uri = "/weibo_callback";

    public String authorizeUrl(String baseurl) {
        return String.format("https://api.weibo.com/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s", weibo_api_key, baseurl + weibo_redirect_uri);
    }*/

    public User getUser(String token) {
        String uid = getUid(token);
        User user = accountService.findByOAuthId(oAuthId(PROVIDER, uid));

        // 第一次登录，创建用户
        if (user == null) {
            Map userInfo = getUserInfo(token, uid);

            user = new User();
            user.setScreenName(newScreenName((String) userInfo.get("screen_name")));
            user.setName((String) userInfo.get("name"));
            user.setProfileImageNormal((String) userInfo.get("profile_image_url"));//50x50
            user.setProfileImageBigger((String) userInfo.get("avatar_large")); //180x180
            user.setProfileImageOriginal((String) userInfo.get("avatar_hd")); //640x640 ?
            LinkedHashSet<String> oAuthIds = new LinkedHashSet<String>(1);
            oAuthIds.add(oAuthId(PROVIDER, uid));
            user.setoAuthIds(oAuthIds);
            user.setWeiboVerified((Boolean) userInfo.get("verified"));

            user = accountService.createUser(user);
        }

        return user;
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

    @Override
    public String getPrivoder() {
        return PROVIDER;
    }
}


