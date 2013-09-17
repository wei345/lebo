package com.lebo.service;

import javapns.Push;
import javapns.devices.Device;
import javapns.notification.PushedNotification;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Apple Push Notification Service.
 *
 * @author: Wei Liu
 * Date: 13-8-7
 * Time: PM3:39
 */
@Service
public class ApnsService {
    private Logger logger = LoggerFactory.getLogger(ApnsService.class);
    @Value("${apns.sound}")
    private String sound;
    @Value("${apns.keystore}")
    private String keystore;
    @Value("${apns.password}")
    private String password;
    @Value("${apns.production}")
    boolean production;

    public void pushMessage(String message, int badge, String deviceToken) {
        try {
            pushMessage(message,
                    badge,
                    sound,
                    ResourceUtils.getFile(keystore),
                    password,
                    production,
                    deviceToken);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("推送通知时发生错误", e);
        }
    }

    /**
     * Push a notification combining an alert, a badge and a sound.
     *
     * @param message    消息的主体内容(set to null to skip).
     * @param badge      显示在程序icon右上角的数字 (set to -1 to skip).
     * @param sound      声音提示文件的文件名，该声音资源文件要在程序包中(set to null to skip).
     * @param keystore   a keystore containing your private key and the certificate signed by Apple (java.io.File,
     *                   java.io.InputStream, byte[], java.security.KeyStore or String for a file path)
     * @param password   keystore的密码
     * @param production true to use Apple's production servers, false to use the sandbox servers
     * @param devices    a list or an array of tokens or devices: String[], List<String>, Device[], List<Device>, String or Device
     */
    private void pushMessage(String message, int badge, String sound, Object keystore, String password, Boolean production, Object devices) {
        long t1 = System.currentTimeMillis();
        logger.debug("正在发送推送通知.. message : {}, badge : {}, sound : {}, devices : {}", message, badge, sound, devices);

        try {
            // alert (message, keystore, password, production, devices): push a simple alert message
            // combined (message, badge, sound, keystore, password, production, devices): push a alert+badge+sound notification
            if (StringUtils.isNotBlank(message)) {
                List<PushedNotification> notifications = Push.combined(message, badge, sound, keystore, password, production, devices);
                //notifications = Push.alert(message, file, pwd, !isDev, device);
                for (PushedNotification notification : notifications) {
                    // Apple accepted the notification and should deliver it
                    if (notification.isSuccessful()) {
                        logger.info("Success push notification");
                    } else {
                        String invalidToken = notification.getDevice().getToken();
                        logger.info("Fail to push notification: " + invalidToken);
                        // Add code here to remove invalidToken from your database
                    }
                }
            } else {
                List<PushedNotification> notifications = Push.badge(badge, keystore, password, production, devices);
                for (PushedNotification notification : notifications) {
                    // Apple accepted the notification and should deliver it
                    if (notification.isSuccessful()) {
                        logger.info("Success push badge");
                    } else {
                        String invalidToken = notification.getDevice().getToken();
                        logger.info("Fail to push badage : " + invalidToken);
                        // TODO Add code here to remove invalidToken from your database
                    }
                }
            }

            logger.debug("发送推送通知成功，{} ms", System.currentTimeMillis() - t1);
        } catch (Exception e) {
            logger.warn("推送通知时出错", e);
        }
    }

    public void removeInactiveDevices() {
        List<Device> inactiveDevices;
        try {
            inactiveDevices = Push.feedback(keystore, password, production);

            for (Device d : inactiveDevices) {
                System.out.println(d.getToken());
                // TODO remove inactive devices from your own list of devices
            }
        } catch (Exception e) {
            logger.warn("删除非活动设备时出错", e);
        }
    }
}
