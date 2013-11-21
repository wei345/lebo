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

    @Autowired
    private MongoTemplate mongoTemplate;

    private Logger logger = LoggerFactory.getLogger(ShiroRenRenLogin.class);

    @Override
    public User getUser(String token) {
        String url = "https://api.renren.com/v2/user/get?access_token=" + token;
        Map map = restTemplate.getForObject(url, Map.class);
        if (map == null || ((Map) map.get("response")).get("name") == null) {
            throw new RuntimeException("获取用户信息发生错误");
        }

        Map userInfo = (Map) map.get("response");
        List<Map<String, String>> avatar = (List) userInfo.get("avatar");

        String uid = userInfo.get("id").toString();
        User user = accountService.findByOAuthId(oAuthId(PROVIDER, uid));

        //新用户
        if (user == null) {

            user = new User();

            //用户名
            user.setScreenName(accountService.generateScreenName((String) userInfo.get("name")));
            user.setName((String) userInfo.get("name"));

            //性别
            Map basicInformation = (Map) userInfo.get("basicInformation");
            user.setGender(getGender((String) basicInformation.get("sex")));

            //头像
            user.setProfileImageNormal(avatar.get(0).get("url")); //50x50
            user.setProfileImageBigger(avatar.get(2).get("url")); //200x180   avatar.get(1).get("url"); //100x100
            user.setProfileImageOriginal(avatar.get(3).get("url")); //200x180?

            //关联登录账号
            LinkedHashSet<String> oAuthIds = new LinkedHashSet<String>(1);
            oAuthIds.add(oAuthId(PROVIDER, uid));
            user.setoAuthIds(oAuthIds);

            //token
            user.setRenrenToken(token);

            user = accountService.createUser(user);
        }
        //老用户
        else {

            //TODO 2014年1月删除此段代码
            //早期，未获取登录用户性别
            Map basicInformation = (Map) userInfo.get("basicInformation");
            Character gender = getGender((String) basicInformation.get("sex"));
            if(gender != null && !gender.equals(user.getGender())){
                mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(user.getId())),
                        new Update().set(User.GENDER_KEY, gender),
                        User.class);

                logger.debug("{}({}) 性别更新为 {}", user.getScreenName(), user.getId(), gender);
            }

            if (!token.equals(user.getRenrenToken())) {

                mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(user.getId())),
                        new Update().set(User.RENREN_TOKEN_KEY, token),
                        User.class);

            }
        }
        return user;
    }/*
    人人网API文档：http://wiki.dev.renren.com/wiki/V2/user/get
    例子：
    GET https://api.renren.com/v2/user/get?access_token=232244|6.554edddb32a4caa024d3c551dd7ee542.2592000.1387447200-235566908
    响应：
    {
	"response": {
		"avatar": [
			{
				"size": "TINY",
				"url": "http://hdn.xnimg.cn/photos/hdn121/20121121/2245/h_tiny_U0pJ_02f0000074c91375.jpg"
			},
			{
				"size": "HEAD",
				"url": "http://hdn.xnimg.cn/photos/hdn121/20121121/2245/h_head_FJ30_02f0000074c91375.jpg"
			},
			{
				"size": "MAIN",
				"url": "http://hdn.xnimg.cn/photos/hdn121/20121121/2245/h_main_VRr3_02f0000074c91375.jpg"
			},
			{
				"size": "LARGE",
				"url": "http://hdn.xnimg.cn/photos/hdn121/20121121/2245/h_large_P1bL_02f0000074c91375.jpg"
			}
		],
		"basicInformation": {
			"birthday": "1985-6-6",
			"homeTown": {
				"city": "沈阳市",
				"province": "辽宁"
			},
			"sex": "FEMALE"
		},
		"education": [
			{
				"department": "文法学院",
				"educationBackground": "MASTER",
				"name": "东北大学",
				"year": "2005"
			}
		],
		"emotionalState": <null>,
		"id": 235566908,
		"like": <null>,
		"name": "谢灵灵",
		"star": 1,
		"work": <null>
	}
}
*/

    @Override
    public String getPrivoder() {
        return PROVIDER;
    }
}
