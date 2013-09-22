package com.lebo.rest;

import com.lebo.entity.Setting;
import com.lebo.rest.dto.RecommendedAppDto;
import com.lebo.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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
    public Object recommendedApps() {
        List<RecommendedAppDto> dtos = new ArrayList<RecommendedAppDto>();

        RecommendedAppDto dto = new RecommendedAppDto();
        dto.setImageUrl("http://file.lebooo.com/images/logo.png");
        dto.setName("乐播");
        dto.setSize("20M");
        dto.setUrl("http://file.dev.lebooo.com/images/btn-dl-lebo-iphone.png");
        dto.setVersion("2.3");
        dto.setBackgroundColor("#abcdef");
        dtos.add(dto);

        dto = new RecommendedAppDto();
        dto.setImageUrl("http://file.lebooo.com/images/logo.png");
        dto.setName("乐播-6秒视频");
        dto.setSize("21M");
        dto.setUrl("http://file.dev.lebooo.com/images/btn-dl-lebo-iphone.png");
        dto.setVersion("2.4");
        dto.setBackgroundColor("#123456");
        dtos.add(dto);

        return dtos;
    }
}
