package com.lebo.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 13-8-20
 * Time: PM7:07
 */
public class ContentTypeMap {

    private static Map<String, String> contentTypeMap = new HashMap<String, String>();

    private static Map<String, String> ext2ContentType = new HashMap<String, String>();

    static {
        //视频
        contentTypeMap.put("video/mp4", "mp4");//mp4 mp4v mpg4
        contentTypeMap.put("video/x-flv", "flv");
        //音频
        contentTypeMap.put("audio/mp4", "mp4a");
        //图片
        contentTypeMap.put("image/jpeg", "jpg"); //jpeg jpg jpe
        contentTypeMap.put("image/png", "png");

        //
        ext2ContentType.put("jpg", "image/jpeg");
        ext2ContentType.put("png", "image/png");
    }

    /**
     * <pre>
     * getExtension(null, null)             =  null
     * getExtension("video/mp4", null)      =  "mp4"
     * getExtension(null, "a.txt")          =  "txt"
     * getExtension("image/jpeg", "a.txt")  =  "jpg"
     * </pre>
     *
     * @param contentType MIME type  http://en.wikipedia.org/wiki/Internet_media_type
     * @param filename    file name
     */
    public static String getExtension(String contentType, String filename) {
        String ext = contentTypeMap.get(contentType);

        if (ext == null) {
            ext = StringUtils.substringAfterLast(filename, ".");
        }

        return ext;
    }

    /**
     * <pre>
     * getContentType(null)     抛出RuntimeException
     * getContentType("jpg")    = image/jpeg
     * getContentType("png")    = image/png
     * </pre>
     */
    public static String getContentType(String ext) {
        if (StringUtils.isBlank(ext)) {
            throw new IllegalArgumentException("扩展名不能为空");
        }

        String contentType = ext2ContentType.get(ext);
        if (contentType == null) {
            throw new RuntimeException(String.format("找不到对应 %s 的ContentType", ext));
        }
        return contentType;
    }
}
