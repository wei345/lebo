package com.lebo.rest.dto;

/**
 * @author: Wei Liu
 * Date: 13-10-16
 * Time: PM4:12
 */
public class PresignedUrlDto {
    private String url;
    private String key;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
