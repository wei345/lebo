package com.lebo.service;

import com.lebo.entity.Ad;
import com.lebo.rest.dto.AdDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-10-11
 * Time: PM5:25
 */
@Service
public class AdService extends AbstractMongoService {

    private static final Sort AD_ORDER = new Sort(Sort.Direction.ASC, Ad.ORDER_KEY);

    public List<Ad> findAd(String group, Boolean enabled) {
        Query query = new Query();

        if (group != null) {
            query.addCriteria(new Criteria(Ad.GROUP_KEY).is(group));
        }

        if (enabled != null) {
            query.addCriteria(new Criteria(Ad.ENABLED_KEY).is(enabled));
        }

        query.with(AD_ORDER);

        return mongoTemplate.find(query, Ad.class);
    }

    public Ad getById(String id) {
        Assert.hasText(id);
        return mongoTemplate.findOne(new Query(new Criteria(Ad.ID_KEY).is(id)), Ad.class);
    }

    public void save(Ad ad) {
        mongoTemplate.save(ad);
    }

    public void delete(String id) {
        Assert.hasText(id);
        mongoTemplate.remove(new Query(new Criteria(Ad.ID_KEY).is(id)), Ad.class);
    }

    public void updateEnabled(String id, boolean enabled) {
        mongoTemplate.updateFirst(new Query(new Criteria(Ad.ID_KEY).is(id)),
                new Update().set(Ad.ENABLED_KEY, enabled), Ad.class);
    }

    public AdDto toDto(Ad ad) {
        AdDto dto = new AdDto();
        dto.setDescription(ad.getDescription());
        dto.setImageUrl(ad.getImageUrl());
        dto.setUrl(ad.getUrl());

        return dto;
    }

    public List<AdDto> toDtos(List<Ad> ads) {
        List<AdDto> dtos = new ArrayList<AdDto>(ads.size());
        for (Ad ad : ads) {
            dtos.add(toDto(ad));
        }
        return dtos;
    }
}
