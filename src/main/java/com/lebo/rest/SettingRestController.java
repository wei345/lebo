package com.lebo.rest;

import com.lebo.entity.Setting;
import com.lebo.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public Object get() {
        Setting setting = settingService.getSetting();
        return settingService.toSettingDto(setting);
    }

    @RequestMapping(value = "channels", method = RequestMethod.GET)
    @ResponseBody
    public Object channels() {
        return settingService.toChannelDtos(settingService.getAllChannels());
    }
}
