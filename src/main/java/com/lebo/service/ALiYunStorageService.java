package com.lebo.service;

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
    String generateTmpWritableUrl(Date expireDate, String contentType, String slug);

}
