package com.lebo.rest.dto;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-9-9
 * Time: AM11:10
 */
public class WeiboFriendDto {
    private List<WeiboUserDto> users;
    private int nextCursor;
    private int totalNumber;

    public List<WeiboUserDto> getUsers() {
        return users;
    }

    public void setUsers(List<WeiboUserDto> users) {
        this.users = users;
    }

    public int getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(int nextCursor) {
        this.nextCursor = nextCursor;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public static class WeiboUserDto{
        String userId; //乐播用户id
        String weiboId;
        String screenName;
        String name;
        String gender; //f,m
        Boolean verified;
        String profileImageUrl;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getWeiboId() {
            return weiboId;
        }

        public void setWeiboId(String weiboId) {
            this.weiboId = weiboId;
        }

        public String getScreenName() {
            return screenName;
        }

        public void setScreenName(String screenName) {
            this.screenName = screenName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Boolean getVerified() {
            return verified;
        }

        public void setVerified(Boolean verified) {
            this.verified = verified;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }
    }
}
