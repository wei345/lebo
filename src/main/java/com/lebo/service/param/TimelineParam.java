package com.lebo.service.param;

/**
 * @author: Wei Liu
 * Date: 13-7-8
 * Time: PM4:46
 */
public class TimelineParam extends PaginationParam {
    private String userId;
    private String screenName;
    private boolean includeReposts = true;
    private boolean includeOriginPosts = true;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isIncludeReposts() {
        return includeReposts;
    }

    public void setIncludeReposts(boolean includeReposts) {
        this.includeReposts = includeReposts;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public boolean isIncludeOriginPosts() {
        return includeOriginPosts;
    }

    public void setIncludeOriginPosts(boolean includeOriginPosts) {
        this.includeOriginPosts = includeOriginPosts;
    }
}
