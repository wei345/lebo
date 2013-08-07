package com.lebo.jms;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * JMS用户变更消息生产者.
 * <p/>
 * 使用jmsTemplate将用户变更消息分别发送到queue与topic.
 *
 * @author calvin
 */
public class ApnsMessageProducer {

    private JmsTemplate jmsTemplate;
    private Destination notifyQueue;

    /**
     * 当用户被关注、喜欢、转播、at、评论、回复评论时，向用户发送通知。
     *
     * @param userId      该用户关注了一个用户
     * @param followingId 该用户被关注了，会收到通知
     */
    //使用jmsTemplate的send/MessageCreator()发送Map类型的消息并在Message中附加属性用于消息过滤.
    public void sendAppPushNotification(final String userId, final String screenName, final String followingId) {
        jmsTemplate.send(notifyQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                MapMessage message = session.createMapMessage();
                message.setString("screenName", screenName);
                message.setString("followingId", followingId);

                message.setStringProperty("objectType", "user");

                return message;
            }
        });
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setNotifyQueue(Destination notifyQueue) {
        this.notifyQueue = notifyQueue;
    }
}
