package com.lebo.service;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.CannedAccessControlList;
import com.aliyun.openservices.oss.model.OSSObject;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.lebo.rest.dto.StatusDto;
import com.lebo.service.param.FileInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-8-17
 * Time: PM12:20
 */
@Service
public class ALiYunStorageService implements FileStorageService {
    @Value("${aliyun_oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun_oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun_oss.bucketName}")
    private String bucketName;
    @Value("${aliyun_oss.endpoint}")
    private String endpoint;
    private OSSClient client;

    private Logger logger = LoggerFactory.getLogger(ALiYunStorageService.class);

    public String key() {
        //用MongoDB规则生成ID
        return new ObjectId().toString();
    }

    @Override
    public String save(InputStream content, String contentType, long contentLength) throws IOException {
        long t1 = System.currentTimeMillis();
        String key = key();

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(contentLength);
        meta.setContentType(contentType);
        PutObjectResult result = client.putObject(bucketName, key, content, meta);

        logger.info("保存文件成功, ETag : {}, {} ms", result.getETag(), (System.currentTimeMillis() - t1));
        return key;
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
    public FileInfo getFileInfo(String id) {
        // 获取Object，返回结果为OSSObject对象
        OSSObject object = client.getObject(bucketName, id);
        ObjectMetadata meta = object.getObjectMetadata();

        // 处理Object
        FileInfo fileInfo = new FileInfo();
        fileInfo.setContent(object.getObjectContent());
        fileInfo.setLength(meta.getContentLength());
        fileInfo.setLastModified(meta.getLastModified().getTime());
        fileInfo.setEtag("W/\"" + fileInfo.getLastModified() + "\"");
        fileInfo.setContentType(meta.getContentType());
        return fileInfo;
    }

    @Override
    public List<String> saveFiles(List<FileInfo> fileInfos) {
        List<String> fileIds = new ArrayList<String>();
        try {
            for (FileInfo fileInfo : fileInfos) {
                fileIds.add(save(fileInfo.getContent(), fileInfo.getContentType(), fileInfo.getLength()));
                IOUtils.closeQuietly(fileInfo.getContent());
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
    public StatusDto.FileInfoDto getFileInfoDto(String id, String contentUrlSuffix) {
        OSSObject object = client.getObject(bucketName, id);
        ObjectMetadata meta = object.getObjectMetadata();

        StatusDto.FileInfoDto dto = new StatusDto.FileInfoDto();
        dto.setContentType(meta.getContentType());
        dto.setContentUrl(getContentUrl(id, contentUrlSuffix));
        dto.setLength(meta.getContentLength());

        return dto;
    }

    private String getContentUrl(String id, String suffix) {
        if (AbstractMongoService.isMongoId(id)) {
            String contentUrl = String.format("http://%s.oss.aliyuncs.com/%s", bucketName, id);
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
