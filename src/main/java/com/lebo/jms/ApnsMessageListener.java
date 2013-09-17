package com.lebo.jms;

import com.lebo.service.ApnsService;
import com.lebo.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 消息的异步被动接收者.
 * <p/>
 * 使用Spring的MessageListenerContainer侦听消息并调用本Listener进行处理.
 *
 * @author calvin
 */
public class ApnsMessageListener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(ApnsMessageListener.class);
    @Autowired
    private ApnsService apnsService;
    @Autowired
    private NotificationService notificationService;

    /**
     * MessageListener回调函数.
     */
    @Override
    public void onMessage(Message message) {
        try {
            MapMessage mapMessage = (MapMessage) message;

            if ("notification".equals(mapMessage.getStringProperty("objectType"))) {
                logger.debug("正在发送APNS通知, 接收者 : {}({}), 内容 : {}, deviceToken : {}",
                        mapMessage.getString("recipientScreenName"), mapMessage.getString("recipientId"),
                        mapMessage.getString("message"), mapMessage.getString("deviceToken"));

                apnsService.pushMessage(mapMessage.getString("message"),
                        notificationService.countUnreadNotifications(mapMessage.getString("recipientId")),
                        mapMessage.getString("deviceToken"));
            }

        } catch (Exception e) {
            logger.error("发送APNS通知时发生异常.", e);
        }
    }
}
