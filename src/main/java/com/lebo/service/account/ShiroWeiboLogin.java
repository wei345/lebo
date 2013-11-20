package com.lebo.service.account;

import com.lebo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(ShiroWeiboLogin.class);

    /*需在开发者账号设置回调URL，iOS客户端在程序内写死了回调URL，不知修改是否有影响，暂不做web微博登录
    private String weibo_redirect_uri = "/weibo_callback";

    public String authorizeUrl(String baseurl) {
        return String.format("https://api.weibo.com/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s", weibo_api_key, baseurl + weibo_redirect_uri);
    }*/

    public User getUser(String token) {
        String uid = weiboService.getUid(token);
        Map userInfo = weiboService.getUserInfo(token, uid);

        User user = accountService.findByOAuthId(oAuthId(PROVIDER, uid));

        // 新用户
        if (user == null) {
            user = new User();
            user.setScreenName(newScreenName((String) userInfo.get("screen_name")));
            user.setName((String) userInfo.get("name"));
            user.setGender(getGender((String) userInfo.get("gender")));
            user.setProfileImageNormal((String) userInfo.get("profile_image_url"));//50x50
            user.setProfileImageBigger((String) userInfo.get("avatar_large")); //180x180
            user.setProfileImageOriginal((String) userInfo.get("avatar_hd")); //640x640 ?
            LinkedHashSet<String> oAuthIds = new LinkedHashSet<String>(1);
            oAuthIds.add(oAuthId(PROVIDER, uid));
            user.setoAuthIds(oAuthIds);
            user.setWeiboVerified((Boolean) userInfo.get("verified"));
            user.setWeiboToken(token);

            accountService.createUser(user);
        }
        //老用户
        else {

            //TODO 2014年1月删除此段代码
            //早期，未获取登录用户性别
            Character gender = getGender((String) userInfo.get("gender"));
            if (gender != null && !gender.equals(user.getGender())) {
                mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(user.getId())),
                        new Update().set(User.GENDER_KEY, gender),
                        User.class);

                logger.debug("{}({}) 性别更新为 {}", user.getScreenName(), user.getId(), gender);
            }

            if (!token.equals(user.getWeiboToken())) {
                mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(user.getId())),
                        new Update().set(User.WEIBO_TOKEN_KEY, token),
                        User.class);
            }

            Boolean verified = (Boolean) userInfo.get("verified");
            if (!verified.equals(user.getVerified())) {
                mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(user.getId())),
                        new Update().set(User.WEIBO_VERIFIED_KEY, verified),
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


