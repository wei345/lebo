package com.lebo.jms;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * 新用户通过新浪微博、人人网等第3放账号登录时，应用会获取用户第3方账号基本信息，为用户创建新账号，立刻返回。
 * 使用消息队列异步获取用户头像图片保存到MongoDB，然后更新用户数据。
 *
 * @author: Wei Liu
 * Date: 13-8-13
 * Time: PM4:01
 */
public class ProfileImageMessageProducer {
    private JmsTemplate jmsTemplate;
    private Destination profileImageQueue;

    public void sendQueue(final String userId, final String profileImageUrl) {
        jmsTemplate.send(profileImageQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("userId", userId);
                mapMessage.setString("profileImageUrl", profileImageUrl);

                mapMessage.setStringProperty("objectType", "user");

                return mapMessage;
            }
        });
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setProfileImageQueue(Destination profileImageQueue) {
        this.profileImageQueue = profileImageQueue;
    }
}
