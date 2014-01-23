package com.lebo.rest;

import com.lebo.entity.Friendship;
import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.WeiboUserDto;
import com.lebo.service.FriendshipService;
import com.lebo.service.account.AbstractOAuthRealm;
import com.lebo.service.account.AccountService;
import com.lebo.service.account.WeiboService;
import com.lebo.service.account.WeiboToken;
import com.lebo.service.param.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

        //排序:双向关注在前
        pageRequest.setSort(new Sort(Sort.Direction.DESC, Friendship.AFB_KEY, Friendship.BFA_KEY));

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
        User currentUser = accountService.getUser(accountService.getCurrentUserId());

        if (token == null) {
            token = currentUser.getFindFriendWeiboToken();
        }

        if (token == null) {
            token = currentUser.getWeiboToken();
        }

        if (token == null) {
            return ErrorDto.FIND_WEIBO_FRIEND_NO_TOKEN;
        }

        String weiboUid;
        //直接使用已存储的weiboUid
        if (token.equals(currentUser.getFindFriendWeiboToken())) {
            weiboUid = currentUser.getFindFriendWeiboUid();
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
        final String WEIBO_FRIEND_CURSOR_KEY = "weibo.friends.cursor";
        //上次查找新浪微博好友的位置
        int cursor = 0;
        if (pageRequest.getPage() > 0) {
            String cursorValue = accountService.getRedisSessionAttribute(WEIBO_FRIEND_CURSOR_KEY);
            if (cursorValue != null) {
                cursor = Integer.valueOf(cursorValue);
            }
        }
        //从新浪微博一次获取多少数据
        int count = 200;//新浪微博每页最多200条
        //已经读取了多少条新浪微博好友
        int readCount = 0;

        //返回客户端的结果
        List<WeiboUserDto> dtos = new ArrayList<WeiboUserDto>(pageRequest.getPageSize());

        while (true) {
            //获取微博好友
            WeiboService.WeiboFriend weiboFriend = weiboService.getFriends(token, weiboUid, count, cursor);

            //遍历新浪微博好友
            for (WeiboService.WeiboUser weiboUser : weiboFriend.getUsers()) {
                cursor++;
                readCount++;
                //查找微博用户对应的乐播用户
                User u = accountService.findByOAuthId(AbstractOAuthRealm.oAuthId(WeiboToken.PROVIDER, weiboUser.getId()));
                //该微博用户也在乐播中
                if (u != null) {
                    WeiboUserDto weiboUserDto = new WeiboUserDto();
                    weiboUserDto.setUserId(u.getId());

                    weiboUserDto.setWeiboId(weiboUser.getId());
                    weiboUserDto.setScreenName(weiboUser.getScreen_name());
                    weiboUserDto.setName(weiboUser.getName());
                    weiboUserDto.setGender(weiboUser.getGender());
                    weiboUserDto.setProfileImageUrl(weiboUser.getProfile_image_url());
                    weiboUserDto.setVerified(weiboUser.getVerified());
                    weiboUserDto.setDescription(weiboUser.getDescription());
                    //是否关注
                    weiboUserDto.setFollowing(friendshipService.isFollowing(currentUser.getId(), u.getId()));
                    if (weiboUserDto.getFollowing()) {
                        weiboUserDto.setBilateral(friendshipService.isBilateral(currentUser.getId(), u.getId()));
                    } else {
                        weiboUserDto.setBilateral(false);
                    }

                    dtos.add(weiboUserDto);
                    //本页已满，跳出循环
                    if (dtos.size() == pageRequest.getPageSize()) {
                        break;
                    }
                }
            }

            if (dtos.size() == pageRequest.getPageSize()) {
                logger.debug("查找新浪好友 : 已满一页数据，结束, token : {}, weiboUid : {}", token, weiboUid);
                break;
            }

            if (weiboFriend.getNext_cursor() == 0) {
                logger.debug("查找新浪好友 : 本次读取 {} 新浪好友，没有更多了，结束, token : {}, weiboUid : {}", readCount, token, weiboUid);
                break;
            }

            if (readCount >= 2000) {//最多读2000条
                logger.debug("找新浪好友 : 本次已读取 2000 新浪好友，结束");
                break;
            }
        }

        //保存游标位置，客户端获取下一页数据时，从这个位置开始查找
        accountService.setRedisSessionAttribute(WEIBO_FRIEND_CURSOR_KEY, String.valueOf(cursor));

        //更新用户的findFriendWeiboToken
        if (!token.equals(currentUser.getFindFriendWeiboToken())) {
            accountService.updateFindFriendWeiboToken(currentUser.getId(), token, weiboUid);
        }

        return dtos;
    }
}
