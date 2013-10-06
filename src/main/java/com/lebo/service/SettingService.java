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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.BeanMapper;

import java.util.ArrayList;
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
        PageRequest pageRequest = new PageRequest(0, 1, PaginationParam.ID_DESC_SORT);
        Page<Setting> page = settingDao.findAll(pageRequest);
        if (page.getContent().size() == 0) {
            setting = new Setting();
        } else {
            setting = page.getContent().get(0);
        }
        return setting;
    }

    public SettingDto toSettingDto(Setting setting) {
        return BeanMapper.map(setting, SettingDto.class);
    }

    public List<Channel> getEnabledChannels() {
        Query query = new Query(new Criteria(Channel.ENABLED_KEY).is(true))
                .with(new Sort(Sort.Direction.ASC, Channel.ORDER_KEY));
        return mongoTemplate.find(query, Channel.class);
    }

    public List<Channel> getAllChannels() {
        return channelDao.findAll(new Sort(Sort.Direction.ASC, Channel.ORDER_KEY));
    }

    public void saveChannel(Channel channel) {
        channelDao.save(channel);
    }

    public void deleteChannel(String id) {
        channelDao.delete(id);
    }

    public Channel getChannel(String id) {
        return channelDao.findOne(id);
    }

    public ChannelDto toChannelDto(Channel channel) {
        return BeanMapper.map(channel, ChannelDto.class);
    }

    public List<ChannelDto> toChannelDtos(List<Channel> channels) {
        List<ChannelDto> dtos = new ArrayList<ChannelDto>(channels.size());
        for (Channel channel : channels) {
            dtos.add(toChannelDto(channel));
        }
        return dtos;
    }

    public void updateChannelEnabled(String id, boolean enabled) {
        mongoTemplate.updateFirst(new Query(new Criteria(Channel.ID_KEY).is(id)),
                new Update().set(Channel.ENABLED_KEY, enabled),
                Channel.class);
    }
}
