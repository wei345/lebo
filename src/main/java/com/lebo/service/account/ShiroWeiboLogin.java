package com.lebo.service.account;

import com.lebo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

    public static final String PROVIDER = "weibo";

    @Autowired
    private WeiboService weiboService;
    @Autowired
    private MongoTemplate mongoTemplate;

    /*private String weibo_redirect_uri = "/weibo_callback";

    public String authorizeUrl(String baseurl) {
        return String.format("https://api.weibo.com/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s", weibo_api_key, baseurl + weibo_redirect_uri);
    }*/

    public User getUser(OauthToken oauthToken) {
        String token = oauthToken.getToken();

        String uid = weiboService.getUid(token);
        Map userInfo = weiboService.getUserInfo(token, uid);

        User user = accountService.findByOAuthId(oAuthId(PROVIDER, uid));

        // 新用户
        if (user == null) {
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
            user.setWeiboToken(token);

            user = accountService.createUser(user);
        }
        //老用户
        else {
            mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(user.getId())),
                    new Update().set(User.WEIBO_VERIFIED_KEY, userInfo.get("verified")).set(User.WEIBO_TOKEN_KEY, token),
                    User.class);
        }

        return user;
    }

    @Override
    public String getPrivoder() {
        return PROVIDER;
    }
}


