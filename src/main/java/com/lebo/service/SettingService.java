package com.lebo.service;

import com.lebo.entity.Setting;
import com.lebo.repository.SettingDao;
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
@Service
public class SettingService extends AbstractMongoService {
    @Autowired
    private SettingDao settingDao;

    private Setting setting;

    public Setting getSetting() {
        //TODO getOption使用缓存，更新时通知所有节点，不要每次都reloadOption
        /*if (setting == null) {
            reloadOption();
        }
        return setting;*/

        return reloadOption();
    }

    public Setting saveOption(Setting setting) {
        return settingDao.save(setting);
    }

    public Setting reloadOption() {
        PageRequest pageRequest = new PageRequest(0, 1, PaginationParam.DEFAULT_SORT);
        Page<Setting> page = settingDao.findAll(pageRequest);
        if (page.getContent().size() == 0) {
            setting = saveOption(new Setting());
        } else {
            setting = page.getContent().get(0);
        }
        return setting;
    }

    public SettingDto toSettingDto(Setting setting) {
        SettingDto dto = BeanMapper.map(setting, SettingDto.class);
        for (SettingDto.ChannelDto channelDto : dto.getChannels()) {
            channelDto.setImageUrl(GridFsService.getContentUrl(channelDto.getImage()));
        }
        return dto;
    }
}
