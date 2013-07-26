package com.lebo.service.param;

/**
 * @author: Wei Liu
 * Date: 13-7-8
 * Time: PM4:46
 */
public class TimelineParam extends PaginationParam {
    private String userId;
    private String screenName;
    private boolean trimUser = false;
    private boolean includeRts = true;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isTrimUser() {
        return trimUser;
    }

    public void setTrimUser(boolean trimUser) {
        this.trimUser = trimUser;
    }

    public boolean isIncludeRts() {
        return includeRts;
    }

    public void setIncludeRts(boolean includeRts) {
        this.includeRts = includeRts;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
