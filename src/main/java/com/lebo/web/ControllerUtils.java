package com.lebo.web;

import com.lebo.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-8-20
 * Time: PM8:55
 */
public class ControllerUtils {
    public static final String MODEL_ERROR_KEY = "error";
    public static final String MODEL_SUCCESS_KEY = "success";
    public static final String AJAX_OK = "ok";

    public static FileInfo getFileInfo(MultipartFile multipartFile) throws IOException {
        return new FileInfo(multipartFile.getInputStream(),
                multipartFile.getContentType(),
                multipartFile.getSize(),
                multipartFile.getOriginalFilename());
    }

}
