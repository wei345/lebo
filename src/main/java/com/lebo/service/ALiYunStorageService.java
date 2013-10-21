package com.lebo.service;

import com.lebo.entity.FileInfo;

import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-8-17
 * Time: PM12:20
 */
public interface ALiYunStorageService extends FileStorageService {
    /**
     * @param expireDate  失效期
     * @param contentType 内容类型如video/mp4
     * @param slug        小写英文字母或数字，单词间以"-"分隔，让人一看url就知道是什么内容
     */
    String generateTmpUploadUrl(Date expireDate, String contentType, String slug);

    /**
     * 返回阿里云云存储URL中的key.
     *
     * @param url 完整url. 如：http://file.dev.lebooo.com/tmp/expire-2013-10-21-15-04-38-post-video-5264c3f61a88a2bb334d44bb.mp4?OSSAccessKeyId=7sKDB271X0Ur9ej0&Expires=1382339078&Signature=KIKelClIZd9J7MiuvcJkkEpyVds%3D
     * @return 返回key.如: tmp/expire-2013-10-21-15-04-38-post-video-5264c3f61a88a2bb334d44bb.mp4 如果不是云存储URL则返回null
     */
    String getKeyFromUrl(String url);

    FileInfo getMetadata(String key);

    /**
     * 判断是否临时文件, 通常以"tmp/"开头的key被认为是临时文件
     */
    boolean isTmpFile(String key);
}
