package com.lebo.service;

import com.lebo.rest.dto.StatusDto;
import com.lebo.service.param.FileInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-8-17
 * Time: PM2:52
 */
public interface FileStorageService {
    String save(InputStream in, String contentType, long contentLength) throws IOException;

    void increaseReferrerCount(String id);

    void decreaseReferrerCount(String id);

    FileInfo getFileInfo(String id);

    void delete(String id);

    List<String> saveFiles(List<FileInfo> fileInfos);

    StatusDto.FileInfoDto getFileInfoDto(String id, String contentUrlSuffix);

    String getContentUrl(String id, String suffix);
}
