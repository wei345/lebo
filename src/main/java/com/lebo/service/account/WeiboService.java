package com.lebo.service.account;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springside.modules.mapper.JsonMapper;

import java.util.List;
import java.util.Map;

/**
 * 访问新浪微博数据。
 *
 * @author: Wei Liu
 * Date: 13-9-9
 * Time: AM9:53
 */
@Service
public class WeiboService {

    protected final RestTemplate restTemplate = new RestTemplate();
    private JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    public String getUid(String token) {
        String url = String.format("https://api.weibo.com/2/account/get_uid.json?access_token=" + token);
        return restTemplate.getForObject(url, Map.class).get("uid").toString();
    }

    public Map getUserInfo(String token, String uid) {
        String url = String.format("https://api.weibo.com/2/users/show.json?uid=%s&access_token=%s", uid, token);
        Map userInfo = restTemplate.getForObject(url, Map.class);

        if (userInfo == null || userInfo.get("name") == null) {
            throw new RuntimeException("获取用户信息发生错误");
        }
        return userInfo;
    }/*
    例子：
    GET https://api.weibo.com/2/users/show.json?uid=3318605793&access_token=2.00txWacDz7QwTC832c8cafd1NOBqCD
    响应:
    {
	"allow_all_act_msg": false,
	"allow_all_comment": true,
	"avatar_hd": "http://tp2.sinaimg.cn/3318605793/180/5667911042/1",
	"avatar_large": "http://tp2.sinaimg.cn/3318605793/180/5667911042/1",
	"bi_followers_count": 0,
	"block_word": 0,
	"city": "1",
	"class": 1,
	"created_at": "Thu Jul 04 21:12:05 +0800 2013",
	"description": "",
	"domain": "",
	"favourites_count": 0,
	"follow_me": false,
	"followers_count": 51,
	"following": false,
	"friends_count": 62,
	"gender": "m",
	"geo_enabled": true,
	"id": 3318605793,
	"idstr": "3318605793",
	"lang": "zh-cn",
	"location": "海外 美国",
	"mbrank": 0,
	"mbtype": 0,
	"name": "垃手",
	"online_status": 0,
	"profile_image_url": "http://tp2.sinaimg.cn/3318605793/50/5667911042/1",
	"profile_url": "u/3318605793",
	"province": "400",
	"ptype": 0,
	"remark": "",
	"screen_name": "垃手",
	"star": 0,
	"status": {
		"attitudes_count": 0,
		"bmiddle_pic": "http://ww2.sinaimg.cn/bmiddle/c5cde7e1jw1e8f9ft7aykj20c807igme.jpg",
		"comments_count": 0,
		"created_at": "Sun Sep 08 18:21:15 +0800 2013",
		"favorited": false,
		"geo": <null>,
		"id": 3620322777695001,
		"idstr": "3620322777695001",
		"in_reply_to_screen_name": "",
		"in_reply_to_status_id": "",
		"in_reply_to_user_id": "",
		"mid": "3620322777695001",
		"mlevel": 0,
		"original_pic": "http://ww2.sinaimg.cn/large/c5cde7e1jw1e8f9ft7aykj20c807igme.jpg",
		"pic_urls": [
			{
				"thumbnail_pic": "http://ww2.sinaimg.cn/thumbnail/c5cde7e1jw1e8f9ft7aykj20c807igme.jpg"
			}
		],
		"reposts_count": 0,
		"source": "<a href=\"http://app.weibo.com/t/feed/48buUD\" rel=\"nofollow\">晚九朝五</a>",
		"text": "夜幕降临，黑暗挑逗激情，霓红闪烁，狂欢驱走寂寞，这里是夜幕下的潮人圣殿，人人都是主角，此时此刻，你来主宰夜晚的开始，从晚九HIGH到天亮。晚上九点，让我们一起开始吧...http://t.cn/zYbfCUY",
		"thumbnail_pic": "http://ww2.sinaimg.cn/thumbnail/c5cde7e1jw1e8f9ft7aykj20c807igme.jpg",
		"truncated": false,
		"visible": {
			"list_id": 0,
			"type": 0
		}
	},
	"statuses_count": 3,
	"url": "",
	"verified": false,
	"verified_reason": "",
	"verified_type": -1.0,
	"weihao": ""
}
    */

    /**
     * @param token
     * @param uid
     * @param count  单页返回的记录条数，最大不超过200。
     * @param cursor 返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，首页为0。
     * @return
     */
    public WeiboFriend getFriends(String token, String uid, int count, int cursor) {
        Assert.hasText(token);
        Assert.hasText(uid);
        Assert.state(count > 0);
        Assert.state(count <= 200);
        Assert.state(cursor >= 0);

        String url = String.format("https://api.weibo.com/2/friendships/friends.json?uid=%s&count=%s&cursor=%s&access_token=%s", uid, count, cursor, token);
        String json = restTemplate.getForObject(url, String.class);
        WeiboFriend weiboFriend = jsonMapper.fromJson(json, WeiboFriend.class);

        if (weiboFriend == null || weiboFriend.getUsers() == null) {
            throw new RuntimeException("获取用户信息发生错误");
        }
        return weiboFriend;
    }

    /*需在开发者账号设置回调URL，iOS客户端在程序内写死了回调URL，不知修改是否有影响，暂不做web微博登录
    private String weibo_redirect_uri = "/weibo_callback";

    public String authorizeUrl(String baseurl) {
        return String.format("https://api.weibo.com/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s", weibo_api_key, baseurl + weibo_redirect_uri);
    }*/

    public static class WeiboFriend {
        private List<WeiboUser> users;
        private int next_cursor;
        private int previous_cursor;
        private int total_number;

        public List<WeiboUser> getUsers() {
            return users;
        }

        public void setUsers(List<WeiboUser> users) {
            this.users = users;
        }

        public int getNext_cursor() {
            return next_cursor;
        }

        public void setNext_cursor(int next_cursor) {
            this.next_cursor = next_cursor;
        }

        public int getPrevious_cursor() {
            return previous_cursor;
        }

        public void setPrevious_cursor(int previous_cursor) {
            this.previous_cursor = previous_cursor;
        }

        public int getTotal_number() {
            return total_number;
        }

        public void setTotal_number(int total_number) {
            this.total_number = total_number;
        }
    }

    public static class WeiboUser {
        private String id;
        private String screen_name;
        private String name;
        private String gender;
        private Boolean verified;
        private String profile_image_url;
        private String description;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getScreen_name() {
            return screen_name;
        }

        public void setScreen_name(String screen_name) {
            this.screen_name = screen_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Boolean getVerified() {
            return verified;
        }

        public void setVerified(Boolean verified) {
            this.verified = verified;
        }

        public String getProfile_image_url() {
            return profile_image_url;
        }

        public void setProfile_image_url(String profile_image_url) {
            this.profile_image_url = profile_image_url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
