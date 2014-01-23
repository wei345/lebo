package com.lebo.service.account;

import com.lebo.entity.User;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 13-12-17
 * Time: AM11:45
 */
@Service
public class WeiboRealm extends AbstractOAuthRealm {

    @Autowired
    private WeiboService weiboService;

    private Logger logger = LoggerFactory.getLogger(WeiboRealm.class);

    @Override
    public boolean supports(AuthenticationToken token) {
        return (token instanceof WeiboToken);
    }

    @Override
    public User getUser(AbstractOAuthToken abstractOAuthToken) {

        WeiboToken weiboToken = (WeiboToken) abstractOAuthToken;

        String token = weiboToken.getToken();

        String uid = weiboService.getUid(token);

        Map userInfo = weiboService.getUserInfo(token, uid);

        User user = getAccountService().findByOAuthId(oAuthId(WeiboToken.PROVIDER, uid));

        // 新用户
        if (user == null) {
            return createUser(userInfo, token, uid);
        }
        //老用户
        else {
            updateUser(user, userInfo, token);
            return user;
        }
    }

    private User createUser(Map userInfo, String token, String uid) {

        User user = new User();
        user.setScreenName(getAccountService().generateScreenName((String) userInfo.get("screen_name")));
        user.setName((String) userInfo.get("name"));
        user.setGender(getGender((String) userInfo.get("gender")));
        user.setProfileImageNormal((String) userInfo.get("profile_image_url"));//50x50
        user.setProfileImageBigger((String) userInfo.get("avatar_large")); //180x180
        user.setProfileImageOriginal((String) userInfo.get("avatar_hd")); //640x640 ?
        LinkedHashSet<String> oAuthIds = new LinkedHashSet<String>(1);
        oAuthIds.add(oAuthId(WeiboToken.PROVIDER, uid));
        user.setoAuthIds(oAuthIds);
        user.setWeiboVerified((Boolean) userInfo.get("verified"));
        user.setWeiboToken(token);

        getAccountService().createUser(user);

        return user;
    }

    private void updateUser(User user, Map userInfo, String token) {

        //TODO 2014年1月删除此段代码
        //早期，未获取登录用户性别
        //更新性别
        String gender = getGender((String) userInfo.get("gender"));

        if (gender != null && !gender.equals(user.getGender())) {

            mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(user.getId())),
                    new Update().set(User.GENDER_KEY, gender),
                    User.class);

            logger.debug("{}({}) 性别更新为 {}", user.getScreenName(), user.getId(), gender);
        }

        //更新token
        if (!token.equals(user.getWeiboToken())) {

            mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(user.getId())),
                    new Update().set(User.WEIBO_TOKEN_KEY, token),
                    User.class);
        }

        //更新V
        Boolean verified = (Boolean) userInfo.get("verified");

        if (!verified.equals(user.getVerified())) {

            mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(user.getId())),
                    new Update().set(User.WEIBO_VERIFIED_KEY, verified),
                    User.class);
        }
    }
}
