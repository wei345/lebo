package com.lebo.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-30
 * Time: AM11:32
 */
public class Suggestions {
    private Users users;
    public static final String USERS_KEY = "users";

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public static class Users {
        private List<User> hot = new ArrayList<User>(1);
        public static final String HOT_KEY = "hot";

        public List<User> getHot() {
            return hot;
        }

        public void setHot(List<User> hot) {
            this.hot = hot;
        }
    }

    public static class User {
        private String userId;
        public static final String USER_ID_KEY = "userId";
        private Integer value;
        public static final String VALUE_KEY = "value";

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }
}

