package com.lebo.service;

import com.lebo.entity.User;
import com.lebo.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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

    private Pattern mentionPattern = Pattern.compile("@([^@\\s]+)");
    private Pattern tagPattern = Pattern.compile("#([^#\\s]+)#");

    public LinkedHashSet<String> mentionUserIds(String text){
        LinkedHashSet<String> userIds = new LinkedHashSet<String>();

        LinkedHashSet<String> names = mentionNames(text);
        for (String screenName : names) {
            User user = userDao.findByScreenName(screenName);
            if(user != null){
                userIds.add(user.getId());
            }
        }
        return userIds;
    }

    LinkedHashSet<String> mentionNames(String text) {
        Matcher m = mentionPattern.matcher(text);
        LinkedHashSet<String> names = new LinkedHashSet<String>();
        while (m.find()) {
            names.add(m.group(1));
        }
        return names;
    }

    public LinkedHashSet<String> findTags(String text){
        Matcher m = tagPattern.matcher(text);
        LinkedHashSet<String> tags = new LinkedHashSet<String>();
        while (m.find()) {
            tags.add(m.group(1));
        }
        return tags;
    }
}
