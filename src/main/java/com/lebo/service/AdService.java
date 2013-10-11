package com.lebo.service;

import com.lebo.entity.Ad;
import com.lebo.rest.dto.AdDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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

    public List<Ad> findAds(String group) {
        Query query = new Query();
        query.addCriteria(new Criteria(Ad.GROUP_KEY).is(group));
        query.with(AD_ORDER);

        return mongoTemplate.find(query, Ad.class);
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
