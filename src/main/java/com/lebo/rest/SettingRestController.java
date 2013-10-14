package com.lebo.rest;

import com.lebo.entity.RecommendedApp;
import com.lebo.entity.Setting;
import com.lebo.service.SettingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-22
 * Time: PM12:36
 */
@Controller
@RequestMapping("/api/1/settings")
public class SettingRestController {
    @Autowired
    private SettingService settingService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Object settings() {
        Setting setting = settingService.getSetting();
        return settingService.toSettingDto(setting);
    }

    @RequestMapping(value = "channels", method = RequestMethod.GET)
    @ResponseBody
    public Object channels() {
        return settingService.toChannelDtos(settingService.getEnabledChannels());
    }

    @RequestMapping(value = "recommendedApps", method = RequestMethod.GET)
    @ResponseBody
    public Object recommendedApps(@RequestHeader("User-Agent") String userAgent) {
        String type = SettingService.RECOMMENDED_APP_TYPE_IOS;
        if (StringUtils.containsIgnoreCase(userAgent, "Android")) {
            type = SettingService.RECOMMENDED_APP_TYPE_ANDROID;
        }

        List<RecommendedApp> recommendedApps = settingService.getEnabledRecommendedApp(type);
        return settingService.toRecommendedAppDtos(recommendedApps, type);
    }
}
