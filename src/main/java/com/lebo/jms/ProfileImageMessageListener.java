package com.lebo.jms;

import com.lebo.entity.User;
import com.lebo.service.GridFsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author: Wei Liu
 * Date: 13-8-13
 * Time: PM4:05
 */
public class ProfileImageMessageListener implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(ProfileImageMessageListener.class);
    @Autowired
    private GridFsService gridFsService;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 将远程文件保存到MongoDB，返回文件ID
     */
    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        String userId = null;
        String profileImageUrl = null;

        HttpURLConnection httpurlconnection = null;
        try {
            userId = mapMessage.getString("userId");
            profileImageUrl = mapMessage.getString("profileImageUrl");

            httpurlconnection = (HttpURLConnection) new URL(profileImageUrl).openConnection();

            if (httpurlconnection.getResponseCode() == 200) {

                //获取文件名
                String filename = "profileImage";
                // Content-Disposition格式："attachment; filename=abc.jpg"
                String raw = httpurlconnection.getHeaderField("Content-Disposition");
                if (raw != null && raw.indexOf("=") != -1) {
                    filename = raw.split("=")[1];
                }

                String fileId = gridFsService.save(httpurlconnection.getInputStream(), filename, httpurlconnection.getContentType());

                mongoTemplate.updateFirst(new Query(new Criteria(User.ID_KEY).is(userId)),
                        new Update().set(User.PROFILE_IMAGE_KEY, fileId), User.class);

                logger.info("获取用户头像保存到数据库成功, userId : {}, profileImageUrl : {}, fileId : {}", userId, profileImageUrl, fileId);
            }
        } catch (Exception e) {
            logger.info("获取用户profileImage失败, userId:{}, profileImageUrl:{}", userId, profileImageUrl);
        } finally {
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
        }
    }
}
