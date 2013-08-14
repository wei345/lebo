package com.lebo.event;

import com.lebo.entity.User;

/**
 * @author: Wei Liu
 * Date: 13-8-14
 * Time: AM10:23
 */
public class AfterUserCreateEvent {
    private User user;

    public AfterUserCreateEvent(User user){
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
