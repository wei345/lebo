package com.lebo.jms;

import com.lebo.entity.User;
import com.lebo.service.account.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
    private AccountService accountService;

    /**
     * 将远程文件保存到MongoDB，返回文件ID
     */
    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        String userId = null;
        HttpURLConnection httpURLConnection = null;

        try {
            userId = mapMessage.getString("userId");
            User user = accountService.getUser(userId);

            //可能在服务器处理此消息时，用户已经更新了图片
            if (!StringUtils.startsWithIgnoreCase(user.getProfileImageOriginal(), "http")) {
                return;
            }
            long t1 = System.currentTimeMillis();
            logger.debug("正在更新 {} 的 profileImage, 原始图片URL: {}", user.getScreenName(), user.getProfileImageOriginal());

            httpURLConnection = (HttpURLConnection) new URL(user.getProfileImageOriginal()).openConnection();

            accountService.saveUserWithProfileImage(user, httpURLConnection.getInputStream());

            logger.info("完成用户头像保存到数据库, {} ms, userId : {}, profileImageOriginal : {}",
                    System.currentTimeMillis() - t1, userId, user.getProfileImageOriginal());
        } catch (Exception e) {
            logger.info("获取用户profileImage失败, userId: {}", userId);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}
