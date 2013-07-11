package com.lebo.service;

import com.lebo.entity.User;
import com.lebo.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Wei Liu
 * Date: 13-7-11
 * Time: PM6:23
 */
@Service
public class MentionService {
    @Autowired
    private UserDao userDao;

    private Pattern mentionPattern = Pattern.compile("@[^@\\s]+");

    public List<String> mentionUserIds(String text){
        List<String> userIds = new ArrayList<String>();

        List<String> nameList = mentionNames(text);
        for (String screenName : nameList) {
            User user = userDao.findByScreenName(screenName);
            if(user != null){
                userIds.add(user.getId());
            }
        }
        return userIds;
    }

    List<String> mentionNames(String text) {
        Matcher m = mentionPattern.matcher(text);
        List<String> names = new ArrayList<String>();
        while (m.find()) {
            names.add(m.group(0).substring(1));
        }
        return names;
    }
}
