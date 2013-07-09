package com.lebo.service;

import com.lebo.entity.User;
import com.lebo.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: AM10:41
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public List searchUser(SearchParam param){
        return userDao.searchUser(param.getQ(), param).getContent();
    }

    public User getUser(String id){
        return userDao.findOne(id);
    }

    public User getUserByScreenName(String screenName){
       return userDao.findByScreenName(screenName);
    }
}
