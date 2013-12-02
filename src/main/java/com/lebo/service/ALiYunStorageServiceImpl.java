package com.lebo.service;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.*;
import com.lebo.entity.FileInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springside.modules.security.utils.Cryptos;
import org.springside.modules.utils.Encodes;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author: Wei Liu
 * Date: 13-10-19
 * Time: AM10:48
 */
public class ALiYunStorageServiceImpl implements ALiYunStorageService {
    @Value("${aliyun_oss.access_key_id}")
    private String accessKeyId;
    @Value("${aliyun_oss.access_key_secret}")
    private String accessKeySecret;
    @Value("${aliyun_oss.bucket_name}")
    private String bucketName;
    @Value("${aliyun_oss.endpoint}")
    private String endpoint;
    @Value("${aliyun_oss.baseurl}")
    private String baseurl;
    private OSSClient client;

    private Logger logger = LoggerFactory.getLogger(ALiYunStorageService.class);
    private static final String KEY_PREFIX_TMP = "tmp/";
    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

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

        //从临时文件copy
        if (isTmpFile(fileInfo.getTmpKey())) {
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucketName, fileInfo.getTmpKey(), bucketName, fileInfo.getKey());
            copyObjectRequest.setNewObjectMetadata(meta);
            client.copyObject(copyObjectRequest);
            client.deleteObject(bucketName, fileInfo.getTmpKey());
            logger.info("保存文件成功: {} -> {}, {} ms", fileInfo.getTmpKey(), fileInfo.getKey(), (System.currentTimeMillis() - t1));
        }
        //保存新文件
        else {
            PutObjectResult result = client.putObject(bucketName, fileInfo.getKey(), fileInfo.getContent(), meta);
            IOUtils.closeQuietly(fileInfo.getContent());
            fileInfo.seteTag(result.getETag());
            logger.info("保存文件成功: {}, {} ms", fileInfo.getKey(), (System.currentTimeMillis() - t1));
        }

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
        fileInfo.setKey(id);
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

    @Override
    public String getKeyFromUrl(String url) {
        String prefix = baseurl + "/";
        //url如：http://file.dev.lebooo.com/tmp/expire-2013-10-21-15-04-38-post-video-5264c3f61a88a2bb334d44bb.mp4?OSSAccessKeyId=7sKDB271X0Ur9ej0&Expires=1382339078&Signature=KIKelClIZd9J7MiuvcJkkEpyVds%3D
        if (StringUtils.startsWith(url, prefix) && url.length() > prefix.length()) {
            String key = url.substring(prefix.length());
            //key如: tmp/expire-2013-10-21-15-04-38-post-video-5264c3f61a88a2bb334d44bb.mp4?OSSAccessKeyId=7sKDB271X0Ur9ej0&Expires=1382339078&Signature=KIKelClIZd9J7MiuvcJkkEpyVds%3D

            int questionMarkPos = key.indexOf("?");
            if (questionMarkPos != -1) {
                key = key.substring(0, questionMarkPos);
                //key如：tmp/expire-2013-10-21-15-04-38-post-video-5264c3f61a88a2bb334d44bb.mp4
            }

            return key;
        }

        return null;
    }

    @Override
    public FileInfo getMetadata(String key) {
        try {
            ObjectMetadata metadata = client.getObjectMetadata(bucketName, key);

            FileInfo fileMetadata = new FileInfo();
            fileMetadata.setLength(metadata.getContentLength());
            fileMetadata.setContentType(metadata.getContentType());
            fileMetadata.setLastModified(metadata.getLastModified().getTime());
            fileMetadata.seteTag(metadata.getETag());
            return fileMetadata;
        } catch (OSSException e) {
            return null;
        }
    }


    /**
     * 判断是否临时文件, 通常以"tmp/"开头的key被认为是临时文件
     */
    @Override
    public boolean isTmpFile(String key) {
        return (key != null && key.startsWith(KEY_PREFIX_TMP));
    }

    @Override
    public String getTmpPath() {
        return KEY_PREFIX_TMP;
    }


    /**
     * @param expireDate  有效期
     * @param path        oss key
     * @param contentType 可为null
     * @return 如：http://oss-example.oss.aliyuncs.com/oss-api.pdf?OSSAccessKeyId=44CF9590006BF252F707&Expires=1141889120&Signature=vjbyPxybdZaNmGa%2ByT272YEAiv4%3D
     */
    @Override
    public String generatePresignedUrl(Date expireDate, String path, String contentType) {
        Assert.notNull(expireDate);
        Assert.notNull(path);

        if ("".equals(path)) {
            //什么都不做
        } else if ("/".equals(path)) {
            path = "";
        } else if (!path.startsWith("/")) {
            path = "/" + path;
        }

        String date = String.valueOf(expireDate.getTime() / 1000);

        String canonicalizedResource = new StringBuilder().append("/").append(bucketName).append(path).toString();

        return new StringBuilder().append(baseurl).append(path)
                .append("?OSSAccessKeyId=").append(accessKeyId)
                .append("&Expires=").append(date)
                .append("&Signature=").append(Encodes.urlEncode(sign("PUT", null, contentType, date, null, canonicalizedResource)))
                .toString();
    }

    String sign(String verb, String contentMd5, String contentType, String date, String canonicalizedOSSHeaders, String canonicalizedResource) {
        Assert.hasText(date);
        Assert.hasText(canonicalizedResource);

        String stringToSign = new StringBuilder().append(StringUtils.trimToEmpty(verb)).append("\n")
                .append(StringUtils.trimToEmpty(contentMd5)).append("\n")
                .append(StringUtils.trimToEmpty(contentType)).append("\n")
                .append(date).append("\n")
                .append(StringUtils.trimToEmpty(canonicalizedOSSHeaders))
                .append(canonicalizedResource)
                .toString();

        return Base64.encodeBase64String(Cryptos.hmacSha1(stringToSign.getBytes(CHARSET_UTF8), accessKeySecret.getBytes(CHARSET_UTF8)));
    }

    @Override
    public FileInfo getTmpFileInfoFromUrl(String tmpUrl) {
        String key = getKeyFromUrl(tmpUrl);
        if (key == null || !isTmpFile(key)) {
            throw new ServiceException("url[" + tmpUrl + "]格式错误");
        }

        FileInfo fileInfo = getMetadata(key);
        if (fileInfo == null) {
            throw new ServiceException("url[" + tmpUrl + "]文件不存在");
        }

        fileInfo.setKey(null);
        fileInfo.setTmpKey(key);

        return fileInfo;
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
