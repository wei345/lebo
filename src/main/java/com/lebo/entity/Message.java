package com.lebo.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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
    //视频(mp4)
    private FileInfo video;
    //视频第一帧(图片)
    private FileInfo videoFirstFrame;
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

    public List<FileInfo> getFiles(){
        List<FileInfo> fileInfos = new ArrayList<FileInfo>(2);
        if(video != null){
            fileInfos.add(video);
        }
        if(videoFirstFrame != null){
            fileInfos.add(videoFirstFrame);
        }
        return fileInfos;
    }

    public FileInfo getVideo() {
        return video;
    }

    public void setVideo(FileInfo video) {
        this.video = video;
    }

    public FileInfo getVideoFirstFrame() {
        return videoFirstFrame;
    }

    public void setVideoFirstFrame(FileInfo videoFirstFrame) {
        this.videoFirstFrame = videoFirstFrame;
    }
}
