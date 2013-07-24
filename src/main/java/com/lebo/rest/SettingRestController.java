package com.lebo.rest;

import com.lebo.entity.Setting;
import com.lebo.rest.dto.SettingDto;
import com.lebo.service.SettingService;
import com.lebo.web.ControllerSetup;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.utils.Encodes;

import java.util.ArrayList;
import java.util.Date;
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
    public Object get() {
        Setting setting = settingService.getSetting();
        SettingDto dto = BeanMapper.map(setting, SettingDto.class);

        String bestContentUrl = "/api/1/statuses/filter";

        List<String> param = new ArrayList<String>();
        if (StringUtils.isNotBlank(setting.getOfficialAccountId())) {
            param.add("follow=" + setting.getOfficialAccountId());
        }

        if (setting.getBestContentDays() != null) {
            Date date = DateUtils.addDays(new Date(), setting.getBestContentDays() * -1);
            String dateStr = ControllerSetup.DEFAULT_DATE_FORMAT.format(date);
            param.add("after=" + Encodes.urlEncode(dateStr));
        }

        if (param.size() > 0) {
            bestContentUrl += "?" + StringUtils.join(param, "&");
        }

        dto.setBestContentUrl(bestContentUrl);

        return dto;
    }
}
