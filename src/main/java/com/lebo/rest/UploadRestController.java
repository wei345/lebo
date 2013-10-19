package com.lebo.rest;

import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.PresignedUrlDto;
import com.lebo.service.ALiYunStorageService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

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
    private ALiYunStorageService aLiYunStorageService;

    public static enum UploadType {
        POST_VIDEO("video/mp4", "post-video", "上传帖子视频"),
        COMMENT_VIDEO("video/mp4", "comment-video", "上传评论视频"),
        COMMENT_AUDIO("audio/amr", "comment-audio", "上传评论音频"),
        IM_VIDEO("video/mp4", "im-video", "上传即时通讯视频"),
        IM_VIDEO_FIRST_FRAME("image/jpeg", "im-video-first-frame", "上传即时通讯视频第一帧"),
        IM_AUDIO("audio/amr", "im-audio", "上传即时通讯音频"),;

        UploadType(String contentType, String slug, String desc) {
            this.contentType = contentType;
            this.slug = slug;
            this.desc = desc;
        }

        String contentType;
        String slug;
        String desc;

        public String getContentType() {
            return contentType;
        }

        public String getSlug() {
            return slug;
        }

        public String getDesc() {
            return desc;
        }
    }

    @RequestMapping(value = "tmpUploadUrl.json", method = RequestMethod.POST)
    @ResponseBody
    public Object tmpUploadUrl(@RequestParam("type") String type) {
        if (StringUtils.isBlank(type)) {
            return ErrorDto.badRequest("type[" + type + "] 不能为空");
        }

        UploadType uploadType = UploadType.valueOf(type.toUpperCase());

        if (uploadType == null) {
            return ErrorDto.badRequest("type[" + type + "]无效");
        }

        //生成签名URL
        Date expDate = DateUtils.addHours(new Date(), 1); //1小时后失效
        String url = aLiYunStorageService.generateTmpWritableUrl(expDate, uploadType.contentType, uploadType.slug);

        //返回结果
        PresignedUrlDto dto = new PresignedUrlDto();
        dto.setUrl(url);
        return dto;
    }

}
