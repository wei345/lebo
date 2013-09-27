package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.WeiboUserDto;
import com.lebo.service.FriendshipService;
import com.lebo.service.account.AbstractOAuthLogin;
import com.lebo.service.account.AccountService;
import com.lebo.service.account.ShiroWeiboLogin;
import com.lebo.service.account.WeiboService;
import com.lebo.service.param.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.nosql.redis.JedisTemplate;
import redis.clients.jedis.Jedis;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户关注的人。
 *
 * @author: Wei Liu
 * Date: 13-7-22
 * Time: PM7:15
 */
@Controller
@RequestMapping("/api/1/friends")
public class FriendRestController {
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private WeiboService weiboService;
    @Autowired
    private JedisTemplate jedisTemplate;
    private Logger logger = LoggerFactory.getLogger(FriendRestController.class);

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "screenName", required = false) String screenName,
                       @Valid PageRequest pageRequest) {
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(screenName)) {
            userId = accountService.getCurrentUserId();
        } else {
            userId = accountService.getUserId(userId, screenName);
        }

        List<String> ids = friendshipService.getFriends(userId, pageRequest);
        return accountService.toUserDtos(accountService.getUsers(ids));
    }

    /**
     * 双向关注。
     */
    @RequestMapping(value = "bilateral", method = RequestMethod.GET)
    @ResponseBody
    public Object bilateral(@RequestParam(value = "userId", required = false) String userId,
                            @RequestParam(value = "screenName", required = false) String screenName,
                            @Valid PageRequest pageRequest) {
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(screenName)) {
            userId = accountService.getCurrentUserId();
        } else {
            userId = accountService.getUserId(userId, screenName);
        }

        List<String> ids = friendshipService.getBilateralFriends(userId, pageRequest);
        return accountService.toUserDtos(accountService.getUsers(ids));
    }

    /**
     * 微博好友。
     * <p/>
     * 为了避免每次查找微博好友登录新浪账号，服务端存储最后一次查找好友使用的token.
     */
    @RequestMapping(value = "weiboFriends", method = RequestMethod.GET)
    @ResponseBody
    public Object weiboFriends(@RequestParam(value = "token", required = false) String token,
                               PageRequest pageRequest) {
        //确定token和weiboUid
        User user = accountService.getUser(accountService.getCurrentUserId());

        if (token == null) {
            token = user.getFindFriendWeiboToken();
        }

        if (token == null) {
            token = user.getWeiboToken();
        }

        if (token == null) {
            return ErrorDto.FIND_WEIBO_FRIEND_NO_TOKEN;
        }

        String weiboUid;
        //直接使用已存储的weiboUid
        if (token.equals(user.getFindFriendWeiboToken())) {
            weiboUid = user.getFindFriendWeiboUid();
        }
        //从新浪获取weiboUid
        else {
            try {
                weiboUid = weiboService.getUid(token);
            } catch (Exception e) {
                return ErrorDto.FIND_WEIBO_FRIEND_ERROR_TOKEN;
            }
        }

        //查找微博好友
        final String weiboFriendCursor = "weibo.friends.cursor";
        int cursor = 0;
        if (pageRequest.getPage() > 0) {
            cursor = jedisTemplate.execute(new JedisTemplate.JedisAction<Integer>() {
                @Override
                public Integer action(Jedis jedis) {
                    String cursor = jedis.hget(accountService.getRedisSessionKey(), weiboFriendCursor);
                    if (cursor == null) {
                        return 0;
                    } else {
                        return Integer.valueOf(cursor);
                    }
                }
            });
        }

        //从redis读取
        int count = Math.min(pageRequest.getPageSize() * 2, 200);//新浪微博每页最多200条
        int readCount = 0; //已经读取了多少条

        List<WeiboUserDto> weiboUserDtos = new ArrayList<WeiboUserDto>(pageRequest.getPageSize());

        while (true) {
            //获取微博好友
            //WeiboService.WeiboFriend weiboFriend = weiboService.getFriends(token, weiboUid, pageRequest.getPageSize(), pageRequest.getOffset());
            WeiboService.WeiboFriend weiboFriend = weiboService.getFriends(token, weiboUid, count, cursor);

            //遍历新浪微博好友
            for (WeiboService.WeiboUser weiboUser : weiboFriend.getUsers()) {
                cursor++;
                readCount++;
                //查找微博用户对应的乐播用户
                User user1 = accountService.findByOAuthId(AbstractOAuthLogin.oAuthId(ShiroWeiboLogin.PROVIDER, weiboUser.getId()));
                //该微博用户也在乐播中
                if (user1 != null) {
                    WeiboUserDto weiboUserDto = new WeiboUserDto();
                    weiboUserDto.setUserId(user1.getId());

                    weiboUserDto.setWeiboId(weiboUser.getId());
                    weiboUserDto.setScreenName(weiboUser.getScreen_name());
                    weiboUserDto.setName(weiboUser.getName());
                    weiboUserDto.setGender(weiboUser.getGender());
                    weiboUserDto.setProfileImageUrl(weiboUser.getProfile_image_url());
                    weiboUserDto.setVerified(weiboUser.getVerified());
                    weiboUserDto.setDescription(weiboUser.getDescription());

                    weiboUserDtos.add(weiboUserDto);
                    //本页已满，跳出循环
                    if (weiboUserDtos.size() == pageRequest.getPageSize()) {
                        break;
                    }
                }
            }

            if (weiboUserDtos.size() == pageRequest.getPageSize()) {
                logger.debug("查找新浪好友 : 已满一页数据，结束, token : {}, weiboUid : {}", token, weiboUid);
                break;
            }

            if (weiboFriend.getNext_cursor() == 0) {
                logger.debug("查找新浪好友 : 本次从新浪读取{}，没有更多了，结束, token : {}, weiboUid : {}", readCount, token, weiboUid);
                break;
            }

            if (readCount >= 2000) {//最多读2000条
                logger.debug("找新浪好友 : 本次已读取 2000 新浪好友，结束");
                break;
            }
        }

        final int cursor1 = cursor;
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.hset(accountService.getRedisSessionKey(), weiboFriendCursor, String.valueOf(cursor1));
            }
        });

        //更新用户的findFriendWeiboToken
        if (!token.equals(user.getFindFriendWeiboToken())) {
            user.setFindFriendWeiboToken(token);
            user.setFindFriendWeiboUid(weiboUid);
            accountService.saveUser(user);
        }

        return weiboUserDtos;
    }
}
