package com.lebo.service;

import com.lebo.entity.Favorite;
import com.lebo.entity.Suggestions;
import com.lebo.entity.User;
import com.lebo.service.account.AccountService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: Wei Liu
 * Date: 13-7-30
 * Time: AM11:55
 */
@Service
public class SuggestionsService extends AbstractMongoService {
    @Autowired
    private AccountService accountService;

    /**
     * 热门用户。
     * <p/>
     * 按24小时内收到的红心数排序。
     */
    public void updateHotUsers() {
        Date oneDayAgo = DateUtils.addDays(dateProvider.getDate(), -1);

        GroupBy groupBy = new GroupBy(Favorite.POST_USERID_KEY);
        groupBy.initialDocument(String.format("{%s:0}", Suggestions.User.VALUE_KEY));
        groupBy.reduceFunction(
                String.format("function(doc, ret){ ret.%s++; ret.%s=doc.%s; }",
                        Suggestions.User.VALUE_KEY,
                        Suggestions.User.USER_ID_KEY,
                        Favorite.POST_USERID_KEY));

        GroupByResults<Suggestions.User> results = mongoTemplate.group(
                new Criteria(Favorite.CREATED_AT_KEY).gt(oneDayAgo),
                mongoTemplate.getCollectionName(Favorite.class),
                groupBy,
                Suggestions.User.class);

        List<Suggestions.User> users = new ArrayList<Suggestions.User>();
        for (Suggestions.User user : results) {
            users.add(user);
        }

        //按红心数降序排序
        Collections.sort(users, new Comparator<Suggestions.User>() {
            @Override
            public int compare(Suggestions.User o1, Suggestions.User o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        //保存前1000条
        if(users.size() > 1000){
            users = users.subList(0, 1000);
        }

        mongoTemplate.upsert(new Query(),
                new Update().set(String.format("%s.%s", Suggestions.USERS_KEY, Suggestions.Users.HOT_KEY), users),
                Suggestions.class);
    }

    public List<User> getHotUsers(Pageable pageable){
        Query query = new Query();
        query.fields().include(String.format("%s.%s", Suggestions.USERS_KEY, Suggestions.Users.HOT_KEY));
        Suggestions suggestions = mongoTemplate.findOne(query, Suggestions.class);

        List<Suggestions.User> hotUsers = suggestions.getUsers().getHot();

        if(hotUsers.size() > pageable.getOffset()){
            hotUsers = hotUsers.subList(pageable.getOffset(),
                    Math.min(hotUsers.size(), pageable.getOffset() + pageable.getPageSize()));
        }

        List<User> users = new ArrayList<User>(hotUsers.size());
        for(Suggestions.User u : hotUsers){
            User user = accountService.getUser(u.getUserId());
            user.setBeFavoritedCount(u.getValue());
            users.add(user);
        }
        return users;
    }
}
