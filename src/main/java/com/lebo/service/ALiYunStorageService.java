package com.lebo.service;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.CannedAccessControlList;
import com.aliyun.openservices.oss.model.OSSObject;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.lebo.entity.FileInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 13-8-17
 * Time: PM12:20
 */
public class ALiYunStorageService implements FileStorageService {
    @Value("${aliyun_oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun_oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun_oss.bucketName}")
    private String bucketName;
    @Value("${aliyun_oss.endpoint}")
    private String endpoint;
    @Value("${aliyun_oss.baseurl}")
    private String baseurl;
    private OSSClient client;

    private Logger logger = LoggerFactory.getLogger(ALiYunStorageService.class);

    @Override
    public String save(FileInfo fileInfo) {
        Assert.hasText(fileInfo.getKey());
        long t1 = System.currentTimeMillis();

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(fileInfo.getLength());
        meta.setContentType(fileInfo.getContentType());
        //保存视频、音频时长
        if (fileInfo.getDuration() != null) {
            Map<String, String> userMeta = new HashMap<String, String>(1);
            userMeta.put("duration", fileInfo.getDuration().toString());
            meta.setUserMetadata(userMeta);
        }

        PutObjectResult result = client.putObject(bucketName, fileInfo.getKey(), fileInfo.getContent(), meta);
        IOUtils.closeQuietly(fileInfo.getContent());

        fileInfo.seteTag(result.getETag());
        logger.info("保存文件成功: {}, {} ms", fileInfo.getKey(), (System.currentTimeMillis() - t1));
        return fileInfo.getKey();
    }

    @Override
    public void increaseReferrerCount(String id) {
        //什么都不做
    }

    @Override
    public void decreaseReferrerCount(String id) {
        //什么都不做
    }

    @Override
    public FileInfo get(String id) {
        // 获取Object，返回结果为OSSObject对象
        OSSObject object = client.getObject(bucketName, id);
        ObjectMetadata meta = object.getObjectMetadata();

        // 处理Object
        FileInfo fileInfo = new FileInfo();
        fileInfo.setContent(object.getObjectContent());
        fileInfo.setLength(meta.getContentLength());
        fileInfo.setLastModified(meta.getLastModified().getTime());
        fileInfo.seteTag("W/\"" + fileInfo.getLastModified() + "\"");
        fileInfo.setContentType(meta.getContentType());
        return fileInfo;
    }

    @Override
    public List<String> save(FileInfo... fileInfos) {
        List<String> fileIds = new ArrayList<String>(fileInfos.length);
        try {
            for (FileInfo fileInfo : fileInfos) {
                fileIds.add(save(fileInfo));
            }
            return fileIds;
        } catch (Exception e) {
            for (String fileId : fileIds) {
                delete(fileId);
            }
            throw new ServiceException("存储文件失败", e);
        }
    }


    @Override
    public void delete(String id) {
        // 删除Object
        client.deleteObject(bucketName, id);
    }

    @Override
    public String getContentUrl(String id, String suffix) {
        if (!StringUtils.startsWithIgnoreCase(id, "http")) {
            String contentUrl = baseurl + "/" + id;
            if (StringUtils.isNotBlank(suffix)) {
                contentUrl += suffix;
            }
            return contentUrl;
        }
        return id;
    }

    @PostConstruct
    public void setUp() {
        client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        boolean exists = client.doesBucketExist(bucketName);

        if (exists) {
            logger.info("Use bucket '{}'", bucketName);
        } else {
            logger.info("Creating bucket '{}' ..", bucketName);
            client.createBucket(bucketName);
            client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }
    }
}
