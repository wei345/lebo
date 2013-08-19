package com.lebo.entity;

import com.lebo.service.FileContentUrlUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.util.Assert;

import java.io.InputStream;

/**
 * 定义Content的基本信息.
 */
public class FileInfo {
    protected String key;
    @Transient
    protected InputStream content;
    protected String filename;
    protected Long length;
    protected String contentType;
    protected Long lastModified;
    protected String eTag;

    public FileInfo() {
    }

    public FileInfo(InputStream content, String contentType, long contentLength) {
        this.content = content;
        this.length = contentLength;
        this.contentType = contentType;
    }

    public InputStream getContent() {
        return content;
    }

    public Long getLength() {
        Assert.notNull(length);
        return length;
    }

    public String getContentType() {
        return contentType;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String geteTag() {
        return eTag;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }


    public void setLength(long length) {
        this.length = length;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentUrl(){
        return FileContentUrlUtils.getContentUrl(key);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id='" + key + '\'' +
                ", content=" + content +
                ", filename='" + filename + '\'' +
                ", length=" + length +
                ", contentType='" + contentType + '\'' +
                ", lastModified=" + lastModified +
                ", eTag='" + eTag + '\'' +
                "} " + super.toString();
    }
}