package com.lebo.service;

import com.lebo.entity.Channel;
import com.lebo.entity.Hashtag;
import com.lebo.repository.HashtagDao;
import com.lebo.rest.dto.ChannelDto;
import com.lebo.service.param.SearchParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.BeanMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-31
 * Time: PM4:02
 */
@Service
public class HashtagService extends AbstractMongoService {
    @Autowired
    private SettingService settingService;
    @Autowired
    private HashtagDao hashtagDao;

    /**
     * 增加指定hashtag的计数，如果hashtag不存在，则新建。
     *
     * @param name hashtag，前后不带#
     */
    public void increasePostsCount(String name) {
        mongoTemplate.upsert(new Query(new Criteria(Hashtag.NAME_KEY).is(name)),
                new Update().inc(Hashtag.POSTS_COUNT_KEY, 1).set(Hashtag.INCREASE_AT_KEY, new Date()),
                Hashtag.class);
    }

    /**
     * 减少指定hashtag的计数，如果计数减少之后为0，则删除该hashtag。
     *
     * @param name hashtag，前后不带#
     */
    public void decreasePostsCount(String name) {
        //该hashtag数量减-1
        mongoTemplate.updateFirst(new Query(new Criteria(Hashtag.NAME_KEY).is(name)),
                new Update().inc(Hashtag.POSTS_COUNT_KEY, -1),
                Hashtag.class);
        //如果该hashtag数量为0，也许数据异常会出现小于0的情况，则删除
        mongoTemplate.remove(
                new Query(new Criteria(Hashtag.NAME_KEY).is(name))
                        .addCriteria(new Criteria(Hashtag.POSTS_COUNT_KEY).lte(0)),
                Hashtag.class);
    }

    public void increaseFavoritesCount(String name) {
        mongoTemplate.upsert(new Query(new Criteria(Hashtag.NAME_KEY).is(name)),
                new Update().inc(Hashtag.FAVORITES_COUNT_KEY, 1),
                Hashtag.class);
    }

    public void decreaseFavoritesCount(String name) {
        mongoTemplate.upsert(new Query(new Criteria(Hashtag.NAME_KEY).is(name)),
                new Update().inc(Hashtag.FAVORITES_COUNT_KEY, -1),
                Hashtag.class);
    }

    public void increaseViewCount(String name) {
        mongoTemplate.upsert(new Query(new Criteria(Hashtag.NAME_KEY).is(name)),
                new Update().inc(Hashtag.VIEW_COUNT_KEY, 1),
                Hashtag.class);
    }

    public List<Hashtag> searchHashtags(SearchParam param) {
        Query query = new Query();

        if (StringUtils.isNotBlank(param.getQ())) {
            query.addCriteria(new Criteria(Hashtag.NAME_KEY).regex(param.getQ(), "i"));
        }

        query.with(param);
        return mongoTemplate.find(query, Hashtag.class);
    }

    public ChannelDto toChannelDto(Hashtag hashtag) {
        List<Channel> channels = settingService.getAllChannels();
        for (Channel channel : channels) {
            if (channel.getName().equals(hashtag.getName())) {
                ChannelDto dto = BeanMapper.map(channel, ChannelDto.class);
                dto.setImageUrl(FileContentUrlUtils.getContentUrl(channel.getImage()));
                return dto;
            }
        }

        return BeanMapper.map(hashtag, ChannelDto.class);
    }

    public List<ChannelDto> toChannelDtos(List<Hashtag> hashtags) {
        List<ChannelDto> dtos = new ArrayList<ChannelDto>(hashtags.size());
        for (Hashtag hashtag : hashtags) {
            dtos.add(toChannelDto(hashtag));
        }
        return dtos;
    }

    public Hashtag findHashtagByName(String name) {
        return hashtagDao.findByName(name);
    }
}
