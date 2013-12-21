package com.lebo.service;

import com.lebo.entity.ActiveUser;
import com.lebo.entity.User;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-12-21
 * Time: PM1:18
 */
@Service
public class ActiveUserService extends AbstractMongoService {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public void incActiveUserCount(String userId) {

        Query query = new Query(new Criteria(User.ID_KEY).is(userId));
        query.fields().include(User.ACTIVE_DAYS_KEY);

        int activeDays = mongoTemplate.findOne(query, User.class).getActiveDays();

        String id = sdf.format(new Date());

        mongoTemplate.upsert(
                new Query(new Criteria(ActiveUser.ID_KEY).is(id)),
                new Update()
                        .inc(ActiveUser.TOTAL_KEY, 1)
                        .inc(getActiveDaysKey(activeDays), 1),
                ActiveUser.class);
    }


    private String getActiveDaysKey(int count) {

        Assert.isTrue(count > 0);

        if (count == 1) {
            return ActiveUser.DAYS_1_KEY;
        }

        if (count == 2) {
            return ActiveUser.DAYS_2_KEY;
        }

        if (count == 3) {
            return ActiveUser.DAYS_3_KEY;
        }

        if (count == 4) {
            return ActiveUser.DAYS_4_KEY;
        }

        if (count == 5) {
            return ActiveUser.DAYS_5_KEY;
        }

        if (count >= 6 && count <= 10) {
            return ActiveUser.DAYS_6_TO_10_KEY;
        }

        if (count >= 11 && count <= 20) {
            return ActiveUser.DAYS_11_TO_20_KEY;
        }

        if (count >= 21 && count <= 50) {
            return ActiveUser.DAYS_21_TO_50_KEY;
        }

        if (count >= 51 && count <= 100) {
            return ActiveUser.DAYS_51_TO_100_KEY;
        }

        if (count >= 101 && count <= 3000) {
            return ActiveUser.DAYS_101_TO_3000_KEY;
        }

        return ActiveUser.DAYS_GTE_3001_KEY;
    }


}
