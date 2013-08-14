package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.event.AfterUserCreateEvent;
import com.lebo.service.FriendshipService;
import com.lebo.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 新用户创建时，自动关注乐播官方账号。
 *
 * @author: Wei Liu
 * Date: 13-8-14
 * Time: AM10:37
 */
@Component
public class AutoFollowOfficialAccountListener {
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private SettingService settingService;

    @Subscribe
    public void followOfficialAccount(AfterUserCreateEvent event){
        friendshipService.follow(event.getUser().getId(), settingService.getSetting().getOfficialAccountId());
    }
}
