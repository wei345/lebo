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
    }

    /**
     *
     * @param token
     * @param uid
     * @param count  单页返回的记录条数，最大不超过200。
     * @param cursor 返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，首页为0。
     * @return
     */
    public WeiboFriend getFriends(String token, String uid, int count, int cursor){
        Assert.hasText(token);
        Assert.hasText(uid);
        Assert.state(count > 0);
        Assert.state(count <= 200);
        Assert.state(cursor >= 0);

        String url = String.format("https://api.weibo.com/2/friendships/friends.json?uid=%s&count=%s&cursor=%s&access_token=%s", uid, count,cursor,token);
        String json = restTemplate.getForObject(url, String.class);
        WeiboFriend weiboFriend = jsonMapper.fromJson(json, WeiboFriend.class);

        if (weiboFriend == null || weiboFriend.getUsers() == null) {
            throw new RuntimeException("获取用户信息发生错误");
        }
        return weiboFriend;
    }

    public static class WeiboFriend{
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

    public static class WeiboUser{
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
