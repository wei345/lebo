package com.lebo.entity;

import com.lebo.rest.dto.FileInfoDto;
import com.lebo.service.FileContentUrlUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义Content的基本信息.
 */
public class FileInfo {
    private String key;
    @Transient
    private InputStream content;
    @Transient
    private String filename;
    private Long length;
    private String contentType;
    private Long lastModified;
    private String eTag;
    private Long duration;//时长，毫秒，文件为视频或音频时，可能有值

    public FileInfo() {
    }

    public FileInfo(InputStream content, String contentType, long contentLength, String filename) {
        this.content = content;
        this.length = contentLength;
        this.contentType = contentType;
        this.filename = filename;
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

    public String getContentUrl() {
        return FileContentUrlUtils.getContentUrl(key);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public FileInfoDto toDto() {
        FileInfoDto dto = new FileInfoDto();
        dto.setContentType(getContentType());
        dto.setContentUrl(getContentUrl());
        dto.setDuration(getDuration());
        dto.setLength(getLength());
        return dto;
    }

    public static List<FileInfoDto> toDtos(List<FileInfo> fileInfos) {
        List<FileInfoDto> dtos = new ArrayList<FileInfoDto>(fileInfos.size());
        for (FileInfo fileInfo : fileInfos) {
            dtos.add(fileInfo.toDto());
        }
        return dtos;
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