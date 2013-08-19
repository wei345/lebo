package com.lebo.service;

import com.lebo.entity.FileInfo;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-8-17
 * Time: PM2:52
 */
public interface FileStorageService {
    /**
     * 设置FileInfo#id，设置FileInfo#eTag(可选), 关闭FileInfo#content
     *
     * @param fileInfo
     * @return id
     */
    String save(FileInfo fileInfo);

    List<String> save(List<FileInfo> fileInfos);

    void increaseReferrerCount(String id);

    void decreaseReferrerCount(String id);

    FileInfo get(String id);

    void delete(String id);

    String getContentUrl(String id, String suffix);
}
