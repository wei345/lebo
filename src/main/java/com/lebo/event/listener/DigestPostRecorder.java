package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.entity.Setting;
import com.lebo.event.BeforePostCreateEvent;
import com.lebo.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Wei Liu
 * Date: 13-8-3
 * Time: PM1:42
 */
@Component
public class DigestPostRecorder {
    @Autowired
    private SettingService settingService;

    @Subscribe
    public void markDigest(BeforePostCreateEvent e) {
        Setting setting = settingService.getSetting();

        //被特定乐播账号转发，记为精华
        if (e.getPost().getOriginPostId() != null && setting.getDigestFollow().contains(e.getPost().getUserId())) {
            e.getPost().setDigest(true);
        } else {
            e.getPost().setDigest(false);
        }
    }
}
