package com.lebo.service.account;

import com.lebo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 13-6-26
 * Time: PM12:09
 */
@Component
public class WeiboOauth {

    public static final String PROVIDER = "weibo";

    @Autowired
    protected AccountService accountService;

    private final RestTemplate restTemplate = new RestTemplate();

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
            // TODO 考虑支持多平台登录，确保screenName唯一
            user.setScreenName((String) userInfo.get("screen_name"));
            user.setName((String) userInfo.get("name"));
            user.setProfileImageUrl((String) userInfo.get("profile_image_url"));
            user.setCreatedAt(new Date());
            LinkedHashSet<String> oAuthIds = new LinkedHashSet<String>(1);
            oAuthIds.add(oAuthId(PROVIDER, uid));
            user.setoAuthIds(oAuthIds);
            user.setLastSignInAt(user.getCreatedAt());
            user = accountService.saveUser(user);
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

    String oAuthId(String provider, String uid) {
        return provider + "/" + uid;
    }
}
