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

    private static Map<String, String> contentType2extension = new HashMap<String, String>();

    private static Map<String, String> extension2ContentType = new HashMap<String, String>();

    static {
        contentType2extension.put("video/mp4", "mp4");//mp4 mp4v mpg4
        contentType2extension.put("video/x-flv", "flv");
        contentType2extension.put("audio/amr", "amr");
        contentType2extension.put("image/jpeg", "jpg"); //jpeg jpg jpe
        contentType2extension.put("image/png", "png");

        extension2ContentType.put("jpg", "image/jpeg");
        extension2ContentType.put("png", "image/png");
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
        String ext = getExtension(contentType);

        if (ext == null) {
            ext = StringUtils.substringAfterLast(filename, ".");
        }

        return ext;
    }

    /**
     * @throws RuntimeException 当找不到指定内容类型的扩展名时
     */
    public static String getExtension(String contentType) {
        String ext = contentType2extension.get(contentType);
        if (ext == null) {
            throw new RuntimeException("找不到contentType[" + contentType + "]的扩展名");
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

        String contentType = extension2ContentType.get(ext);
        if (contentType == null) {
            throw new RuntimeException(String.format("找不到对应 %s 的ContentType", ext));
        }
        return contentType;
    }
}
