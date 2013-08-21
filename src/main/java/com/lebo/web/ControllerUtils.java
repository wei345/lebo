package com.lebo.web;

import com.lebo.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: Wei Liu
 * Date: 13-8-20
 * Time: PM8:55
 */
public class ControllerUtils {
    public static FileInfo getFileInfo(MultipartFile multipartFile) throws IOException {
        return new FileInfo(multipartFile.getInputStream(),
                multipartFile.getContentType(),
                multipartFile.getSize(),
                multipartFile.getOriginalFilename());
    }
}
