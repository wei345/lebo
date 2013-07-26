package com.lebo.entity;

import com.lebo.web.ControllerSetup;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springside.modules.utils.Encodes;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 应用选项。
 *
 * @author: Wei Liu
 * Date: 13-7-18
 * Time: PM4:18
 */
@Document
public class Setting extends IdEntity {
    private List<Channel> channels = new ArrayList<Channel>();
    private String officialAccountId;

    private Integer digestDays = 2;
    private Integer hotDays = 2;
    private LinkedHashSet<String> digestFollow;

    public String getOfficialAccountId() {
        return officialAccountId;
    }

    public void setOfficialAccountId(String officialAccountId) {
        this.officialAccountId = officialAccountId;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public Integer getDigestDays() {
        return digestDays;
    }

    public void setDigestDays(Integer digestDays) {
        this.digestDays = digestDays;
    }

    /**
     * 热门。按红心数(收藏数)排序，最近2天
     */
    public String getReMen() {
        String url = "/api/1/statuses/search.json";

        List<String> param = new ArrayList<String>();
        param.add("orderBy=favoritesCount");
        param.add("order=desc");

        if (hotDays != null) {
            Date date = DateUtils.addDays(new Date(), hotDays * -1);
            String dateStr = ControllerSetup.DEFAULT_DATE_FORMAT.format(date);
            param.add("after=" + Encodes.urlEncode(dateStr));
        }

        if (param.size() > 0) {
            url += "?" + StringUtils.join(param, "&");
        }

        return url;
    }

    /**
     * 精华。由乐播官方账号转发，最近多少天的内容
     */
    public String getJingHua() {
        String url = "/api/1/statuses/filter.json";

        List<String> param = new ArrayList<String>();
        if (StringUtils.isNotBlank(officialAccountId)) {
            param.add("follow=" + officialAccountId);
        }

        if (digestDays != null) {
            Date date = DateUtils.addDays(new Date(), digestDays * -1);
            String dateStr = ControllerSetup.DEFAULT_DATE_FORMAT.format(date);
            param.add("after=" + Encodes.urlEncode(dateStr));
        }

        if (param.size() > 0) {
            url += "?" + StringUtils.join(param, "&");
        }

        return url;
    }

    public Integer getHotDays() {
        return hotDays;
    }

    public void setHotDays(Integer hotDays) {
        this.hotDays = hotDays;
    }

    public LinkedHashSet<String> getDigestFollow() {
        return digestFollow;
    }

    public void setDigestFollow(LinkedHashSet<String> digestFollow) {
        this.digestFollow = digestFollow;
    }

    public static class Channel {
        private String name;
        private String description;
        private String contentUrl;
        private String image;
        private String backgroundColor;
        private boolean enabled;

        public Channel() {

        }

        public Channel(String name, String contentUrl, String image, String backgroundColor, boolean enabled) {
            this.name = name;
            this.contentUrl = contentUrl;
            this.image = image;
            this.backgroundColor = backgroundColor;
            this.enabled = enabled;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
