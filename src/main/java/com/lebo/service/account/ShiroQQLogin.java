package com.lebo.service.account;

import com.lebo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.JsonMapper;

import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 13-11-19
 * Time: PM2:56
 */
@Service
public class ShiroQQLogin extends AbstractOAuthLogin {
    public static String PROVIDER = "qq";

    @Value("${qq_appid}")
    private String qqAppId;
    @Autowired
    private MongoTemplate mongoTemplate;

    private JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    private static final Integer SUCCESS_CODE = 0;

    @Override
    public User getUser(OauthToken oauthToken) {
        String token = oauthToken.getToken();
        String uid = oauthToken.getUid();

        String url = new StringBuilder("https://graph.qq.com/user/get_user_info")
                .append("?oauth_consumer_key=").append(qqAppId)
                .append("&access_token=").append(token)
                .append("&openid=").append(uid).toString();

        String json = restTemplate.getForObject(url, String.class);
        Map userInfo = jsonMapper.fromJson(json, Map.class);

        if (!SUCCESS_CODE.equals(userInfo.get("ret"))) {
            //QQ验证失败
            return null;
        }

        //QQ验证通过

        User user = accountService.findByOAuthId(oAuthId(PROVIDER, uid));

        // 新用户
        if (user == null) {

            user = new User();
            user.setScreenName(newScreenName((String) userInfo.get("nickname")));
            user.setName((String) userInfo.get("nickname"));
            user.setProfileImageNormal((String) userInfo.get("figureurl_qq_1")); //40x40
            user.setProfileImageBigger((String) userInfo.get("figureurl_qq_2")); //100x100
            user.setProfileImageOriginal((String) userInfo.get("figureurl_qq_2")); //100x100
            LinkedHashSet<String> oAuthIds = new LinkedHashSet<String>(1);
            oAuthIds.add(oAuthId(PROVIDER, uid));
            user.setoAuthIds(oAuthIds);
            user.setQqToken(token);

            accountService.createUser(user);
        }
        //老用户
        else {
            if (!token.equals(user.getQqToken())) {

                mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(user.getId())),
                        new Update().set(User.QQ_TOKEN_KEY, token),
                        User.class);

            }
        }

        return user;
    }

    @Override
    public String getPrivoder() {
        return PROVIDER;
    }
}
