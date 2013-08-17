package com.lebo.service.param;

import java.io.InputStream;

/**
 * 定义Content的基本信息.
 */
public class FileInfo {
    protected InputStream content;
    protected String filename;
    protected long length;
    protected String contentType;
    protected long lastModified;
    protected String etag;

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

    public long getLength() {
        return length;
    }

    public String getContentType() {
        return contentType;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getEtag() {
        return etag;
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

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}