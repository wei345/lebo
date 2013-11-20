package com.lebo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-11-6
 * Time: PM7:31
 */
@Document(collection = "uploadurls")
public class UploadUrl extends IdEntity {
    private String url;
    private String path;
    private String contentType;
    private Date expireAt; //签名失效时间
    private Date createdAt;
    private Date deleteAt; //删除文件时间
    public static final String DELETE_AT_KEY = "deleteAt";

    public UploadUrl() {
    }

    public UploadUrl(String url, String path, String contentType, Date expireAt, Date createdAt, Date deleteAt) {
        this.url = url;
        this.path = path;
        this.contentType = contentType;
        this.expireAt = expireAt;
        this.createdAt = createdAt;
        this.deleteAt = deleteAt;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(Date deleteAt) {
        this.deleteAt = deleteAt;
    }
}
