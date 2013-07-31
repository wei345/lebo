package com.lebo.service;

import com.lebo.entity.Hashtag;
import com.lebo.service.param.SearchParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-31
 * Time: PM4:02
 */
@Service
public class HashtagService extends AbstractMongoService {

    public void increaseCount(String name) {
        mongoTemplate.upsert(new Query(new Criteria(Hashtag.NAME_KEY).is(name)),
                new Update().inc(Hashtag.COUNT_KEY, 1).set(Hashtag.INCREASE_AT_KEY, new Date()),
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
}
