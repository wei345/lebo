package com.lebo.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Messages are short, non-public messages sent between two users.
 *
 * @author: Wei Liu
 * Date: 13-7-14
 * Time: PM5:56
 */
@Document
public class Message extends IdEntity {
    @Indexed
    private String from;
    @Indexed
    private String to;
    private String text;
    private List<String> files;
    private Date createdAt;
    //来源，如：手机客户端、网页版
    private String source;
    private GeoLocation geoLocation;
    @Indexed
    private LinkedHashSet<String> searchTerms;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public LinkedHashSet<String> getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(LinkedHashSet<String> searchTerms) {
        this.searchTerms = searchTerms;
    }
}
