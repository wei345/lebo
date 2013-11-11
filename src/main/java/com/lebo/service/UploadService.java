package com.lebo.service;

import com.lebo.entity.UploadUrl;
import com.lebo.util.ContentTypeMap;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-11-6
 * Time: PM8:04
 */
@Service
public class UploadService extends AbstractMongoService {

    @Autowired
    private ALiYunStorageService aLiYunStorageService;

    private SimpleDateFormat uploadPathDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private Logger logger = LoggerFactory.getLogger(UploadService.class);

    public String newTmpUploadUrl(Date expireDate, String contentType, String slug) {

        String path = generateTmpUploadPath(expireDate, contentType, slug);
        String url = aLiYunStorageService.generatePresignedUrl(expireDate, path, contentType);

        mongoTemplate.save(
                new UploadUrl(url, path, contentType, expireDate, new Date(), expireDate),
                mongoTemplate.getCollectionName(UploadUrl.class));

        logger.debug("已生成新的上传地址: " + url);

        return url;
    }

    /**
     * @param expireDate  失效期
     * @param contentType 内容类型如video/mp4
     * @param slug        小写英文字母或数字，单词间以"-"分隔，让人一看url就知道是什么内容
     */
    String generateTmpUploadPath(Date expireDate, String contentType, String slug) {
        Assert.notNull(expireDate);
        Assert.notNull(contentType);
        Assert.notNull(slug);

        return new StringBuilder(aLiYunStorageService.getTmpPath()).append("expire")
                .append("-").append(uploadPathDateFormat.format(expireDate))
                .append("-").append(slug.toLowerCase())
                .append("-").append(new ObjectId().toString()) //使用MongoDB ID格式，只因为够短够简单
                .append(".").append(ContentTypeMap.getExtension(contentType))
                .toString();
    }

    /**
     * 清理过期的上传地址及上传的文件
     *
     * @return 清理数量
     */
    public int cleanExpireUrl() {
        List<UploadUrl> uploadUrls = mongoTemplate.find(
                new Query(new Criteria(UploadUrl.DELETE_AT_KEY).lt(new Date())),
                UploadUrl.class);

        for (UploadUrl uploadUrl : uploadUrls) {
            aLiYunStorageService.delete(uploadUrl.getPath());
            mongoTemplate.remove(uploadUrl);
            logger.debug("已删除过期文件: " + uploadUrl.getPath());
        }

        return uploadUrls.size();
    }

    public String newImUploadUrl(String contentType) {
        Assert.notNull(contentType);

        String path = new StringBuilder(ImService.FILE_COLLECTION_NAME + "/")
                .append(new ObjectId())
                .append(".").append(ContentTypeMap.getExtension(contentType))
                .toString();

        Date expireDate = DateUtils.addHours(new Date(), 1);
        Date deleteDate = DateUtils.addWeeks(expireDate, 1);

        String url = aLiYunStorageService.generatePresignedUrl(
                expireDate,
                path,
                contentType);

        mongoTemplate.save(
                new UploadUrl(url, path, contentType, expireDate, new Date(), deleteDate),
                mongoTemplate.getCollectionName(UploadUrl.class));

        logger.debug("已生成新的上传地址: " + url);

        return url;
    }
}
