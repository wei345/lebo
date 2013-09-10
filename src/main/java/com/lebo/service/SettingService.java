package com.lebo.service;

import com.lebo.entity.Channel;
import com.lebo.entity.Setting;
import com.lebo.jms.SettingMessageProducer;
import com.lebo.repository.ChannelDao;
import com.lebo.repository.SettingDao;
import com.lebo.rest.dto.ChannelDto;
import com.lebo.rest.dto.SettingDto;
import com.lebo.service.param.PaginationParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.BeanMapper;

import java.util.List;

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
    private ChannelDao channelDao;
    @Autowired
    private SettingMessageProducer settingMessageProducer;
    private Setting setting;

    public Setting getSetting() {
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
            channelDto.setImageUrl(FileContentUrlUtils.getContentUrl(channelDto.getImage()));
        }
        return dto;
    }

    public List<Channel> getAllChannels(){
        return channelDao.findAll(new Sort(Sort.Direction.ASC, Channel.ORDER_KEY));
    }

    public void saveChannel(Channel channel){
        channelDao.save(channel);
    }

    public void deleteChannel(String id){
        channelDao.delete(id);
    }

    public Channel getChannel(String id){
        return channelDao.findOne(id);
    }
}
