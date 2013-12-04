package com.lebo.rest.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-12-4
 * Time: PM8:37
 */
public class GiverRankingDto {

    private Giver me;

    private List<Giver> giverList = new ArrayList<Giver>();

    public Giver getMe() {
        return me;
    }

    public void setMe(Giver me) {
        this.me = me;
    }

    public List<Giver> getGiverList() {
        return giverList;
    }

    public void setGiverList(List<Giver> giverList) {
        this.giverList = giverList;
    }

    public static class Giver {
        private String id;
        private String screenName;
        private String profileImageUrl;
        private Integer giveValue;
        private Integer rank;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getScreenName() {
            return screenName;
        }

        public void setScreenName(String screenName) {
            this.screenName = screenName;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }

        public Integer getGiveValue() {
            return giveValue;
        }

        public void setGiveValue(Integer giveValue) {
            this.giveValue = giveValue;
        }

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }
    }

}
