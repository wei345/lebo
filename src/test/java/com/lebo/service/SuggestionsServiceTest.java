package com.lebo.service;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.Suggestions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-30
 * Time: PM3:03
 */
public class SuggestionsServiceTest extends SpringContextTestCase {
    @Autowired
    private SuggestionsService suggestionsService;

    @Test
    public void updateHotUsers() {
        suggestionsService.updateHotUsers();
    }

    @Test
    public void sort() {
        List<Suggestions.User> users = new ArrayList<Suggestions.User>();

        Suggestions.User user = new Suggestions.User();
        user.setUserId("1");
        user.setValue(3);
        users.add(user);

        user = new Suggestions.User();
        user.setUserId("2");
        user.setValue(2);
        users.add(user);

        user = new Suggestions.User();
        user.setUserId("3");
        user.setValue(1);
        users.add(user);

        user = new Suggestions.User();
        user.setUserId("4");
        user.setValue(4);
        users.add(user);

        //按红心数降序排序
        Collections.sort(users, new Comparator<Suggestions.User>() {
            @Override
            public int compare(Suggestions.User o1, Suggestions.User o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        users = users.subList(0, 8);

        for (Suggestions.User u : users) {
            System.out.println(String.format("userId:%s, value:%s", u.getUserId(), u.getValue()));
        }
    }
}
