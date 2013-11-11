package com.lebo.rest;

import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.PresignedUrlDto;
import com.lebo.service.UploadService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 处理上传.
 *
 * @author: Wei Liu
 * Date: 13-10-14
 * Time: PM3:23
 */
@RequestMapping("/api/1.1/upload/*")
@Controller
public class UploadRestController {
    @Autowired
    private UploadService uploadService;

    public static LinkedHashSet<String> allowedContentType;

    static {
        allowedContentType = new LinkedHashSet<String>(4);
        allowedContentType.add("video/mp4");
        allowedContentType.add("image/jpeg");
        allowedContentType.add("image/png");
        allowedContentType.add("audio/amr");
    }

    @RequestMapping(value = "newTmpUploadUrl.json", method = RequestMethod.GET)
    @ResponseBody
    public Object tmpUploadUrl(@RequestParam("contentType") String contentType) {
        if (!allowedContentType.contains(contentType)) {
            return ErrorDto.badRequest("contentType必须为以下值之一：" + allowedContentType);
        }

        //生成签名URL
        Date expireDate = DateUtils.addHours(new Date(), 1); //1小时后失效
        String url = uploadService.newTmpUploadUrl(expireDate, contentType, contentType.replace("/", "-"));

        //返回结果
        PresignedUrlDto dto = new PresignedUrlDto();
        dto.setUrl(url);
        return dto;
    }

    @RequestMapping(value = "newImUploadUrls.json", method = RequestMethod.GET)
    @ResponseBody
    public Object newImUploadUrls(@RequestParam("contentType") String[] contentTypes) {

        List<String> urls = new ArrayList<String>(contentTypes.length);

        for (String contentType : contentTypes) {
            if (!allowedContentType.contains(contentType)) {
                return ErrorDto.badRequest("contentType必须为以下值之一：" + allowedContentType);
            }

            urls.add(uploadService.newImUploadUrl(contentType));
        }

        return urls;
    }

}
