package com.lebo.event;

import com.lebo.entity.User;

/**
 * 用户成功登录后，触发此事件。
 *
 * @author: Wei Liu
 * Date: 13-8-15
 * Time: PM9:26
 */
public class AfterUserLoginEvent {
    private User user;

    public AfterUserLoginEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
