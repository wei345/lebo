package com.lebo.service;

import com.lebo.entity.Setting;
import com.lebo.jms.SettingMessageProducer;
import com.lebo.repository.SettingDao;
import com.lebo.rest.dto.ChannelDto;
import com.lebo.rest.dto.SettingDto;
import com.lebo.service.param.PaginationParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.BeanMapper;

/**
 * @author: Wei Liu
 * Date: 13-7-18
 * Time: PM5:27
 */
//TODO 定时计算频道总帖子数、红心(收藏)数、浏览数
@Service
public class SettingService extends AbstractMongoService {
    @Autowired
    private SettingDao settingDao;
    @Autowired
    private SettingMessageProducer settingMessageProducer;
    private Setting setting;

    public Setting getSetting() {
        //TODO getOption使用缓存，更新时通知所有节点，不要每次都reloadOption
        if (setting == null) {
            reloadSetting();
        }
        return setting;
    }

    public Setting saveSetting(Setting setting) {
        setting = settingDao.save(setting);
        settingMessageProducer.sendTopic();
        return setting;
    }

    public Setting reloadSetting() {
        PageRequest pageRequest = new PageRequest(0, 1, PaginationParam.DEFAULT_SORT);
        Page<Setting> page = settingDao.findAll(pageRequest);
        if (page.getContent().size() == 0) {
            setting = new Setting();
        } else {
            setting = page.getContent().get(0);
        }
        return setting;
    }

    public SettingDto toSettingDto(Setting setting) {
        SettingDto dto = BeanMapper.map(setting, SettingDto.class);
        for (ChannelDto channelDto : dto.getChannels()) {
            channelDto.setImageUrl(GridFsService.getContentUrl(channelDto.getImage()));
        }
        return dto;
    }
}
