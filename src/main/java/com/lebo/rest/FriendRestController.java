package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.WeiboFriendDto;
import com.lebo.service.FriendshipService;
import com.lebo.service.account.AbstractOAuthLogin;
import com.lebo.service.account.AccountService;
import com.lebo.service.account.WeiboService;
import com.lebo.service.param.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
                               @RequestParam(value = "count", defaultValue = "50") int count,
                               @RequestParam(value = "cursor", defaultValue = "0") int cursor) {
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

        //获取微博好友
        WeiboService.WeiboFriend weiboFriend = weiboService.getFriends(token, weiboUid, count, cursor);

        WeiboFriendDto dto = new WeiboFriendDto(); //返回结果
        dto.setNextCursor(weiboFriend.getNext_cursor());
        dto.setTotalNumber(weiboFriend.getTotal_number());

        List<WeiboFriendDto.WeiboUserDto> weiboUserDtos = new ArrayList<WeiboFriendDto.WeiboUserDto>(weiboFriend.getUsers().size());
        //遍历新浪微博好友
        for (WeiboService.WeiboUser weiboUser : weiboFriend.getUsers()) {
            //微博用户 -> 乐播用户
            User user1 = accountService.findByOAuthId(AbstractOAuthLogin.oAuthId("weibo", weiboUser.getId()));

            WeiboFriendDto.WeiboUserDto weiboUserDto = new WeiboFriendDto.WeiboUserDto();
            //该微博用户也在乐播中
            if (user1 != null) {
                weiboUserDto.setUserId(user1.getId());
            }
            weiboUserDto.setWeiboId(weiboUser.getId());
            weiboUserDto.setScreenName(weiboUser.getScreen_name());
            weiboUserDto.setName(weiboUser.getName());
            weiboUserDto.setGender(weiboUser.getGender());
            weiboUserDto.setProfileImageUrl(weiboUser.getProfile_image_url());
            weiboUserDto.setVerified(weiboUser.getVerified());

            weiboUserDtos.add(weiboUserDto);
        }
        dto.setUsers(weiboUserDtos);

        //更新用户的findFriendWeiboToken
        if (!token.equals(user.getFindFriendWeiboToken())) {
            user.setFindFriendWeiboToken(token);
            user.setFindFriendWeiboUid(weiboUid);
            accountService.saveUser(user);
        }

        return dto;
    }
}
