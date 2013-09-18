package com.lebo.rest;

import com.lebo.entity.Setting;
import com.lebo.rest.dto.HotUserListSettingDto;
import com.lebo.rest.dto.UserDto;
import com.lebo.service.SettingService;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.BeanMapper;

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
    @Autowired
    private AccountService accountService;


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

    /**
     * 红人榜页
     */
    @RequestMapping(value = "hotUserList", method = RequestMethod.GET)
    @ResponseBody
    public Object hotUserList(@RequestParam(value = "count", defaultValue = "0") int count) {
        Setting setting = settingService.getSetting();
        HotUserListSettingDto dto = BeanMapper.map(setting, HotUserListSettingDto.class);

        if (count > 0) {
            List<UserDto> users = accountService.getHotUsers(0, count);
            dto.setUsers(users);
        }
        return dto;
    }
}
