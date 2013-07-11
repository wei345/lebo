package com.lebo.service.param;

import java.io.InputStream;

/**
 * 定义Content的基本信息.
 */
public class FileInfo {
    protected InputStream content;
    protected String filename;
    protected int length;
    protected String mimeType;
    protected long lastModified;
    protected String etag;

    public FileInfo() {
    }

    public FileInfo(InputStream content, String filename, String mimeType) {
        this.content = content;
        this.filename = filename;
        this.mimeType = mimeType;
    }

    public InputStream getContent() {
        return content;
    }

    public int getLength() {
        return length;
    }

    public String getMimeType() {
        return mimeType;
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


    public void setLength(int length) {
        this.length = length;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
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