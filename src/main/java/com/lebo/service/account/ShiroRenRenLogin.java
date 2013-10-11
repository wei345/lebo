package com.lebo.service.account;

import com.lebo.entity.User;
import org.springframework.stereotype.Service;

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
    public static String PROVIDER = "renren";

    @Override
    public User getUser(String token) {
        Map userInfo = (Map) getUserInfo(token).get("response");
        List<Map<String, String>> avatar = (List) userInfo.get("avatar");

        String uid = userInfo.get("id").toString();
        User user = accountService.findByOAuthId(oAuthId(PROVIDER, uid));

        // 第一次登录，创建用户
        if (user == null) {

            user = new User();
            user.setScreenName(newScreenName((String) userInfo.get("name")));
            user.setName((String) userInfo.get("name"));
            user.setProfileImageNormal(avatar.get(0).get("url")); //50x50
            user.setProfileImageBigger(avatar.get(2).get("url")); //200x180   avatar.get(1).get("url"); //100x100
            user.setProfileImageOriginal(avatar.get(3).get("url")); //200x180?
            LinkedHashSet<String> oAuthIds = new LinkedHashSet<String>(1);
            oAuthIds.add(oAuthId(PROVIDER, uid));
            user.setoAuthIds(oAuthIds);
            user.setRenrenToken(token);

            user = accountService.createUser(user);
        }
        //更新token
        else {
            if (!token.equals(user.getRenrenToken())) {
                user.setRenrenToken(token);
                accountService.saveUser(user);
            }
        }
        return user;
    }

    @Override
    public String getPrivoder() {
        return PROVIDER;
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
