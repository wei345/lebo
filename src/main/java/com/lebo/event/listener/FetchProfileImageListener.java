package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.event.AfterUserCreateEvent;
import com.lebo.jms.ProfileImageMessageProducer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Wei Liu
 * Date: 13-8-16
 * Time: PM2:52
 */
@Component
public class FetchProfileImageListener {
    @Autowired
    private ProfileImageMessageProducer profileImageMessageProducer;

    /**
     * 获取用户头像存到本地数据库
     */
    @Subscribe
    public void fetchProfileImage(AfterUserCreateEvent event) {
        if (StringUtils.startsWithIgnoreCase(event.getUser().getProfileImageOriginal(), "http")) {
            profileImageMessageProducer.sendQueue(event.getUser().getId(), event.getUser().getProfileImage());
        }
    }
}
