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
     *
     * @param message     ios通知内容
     * @param deviceToken ios设备token
     */
    //使用jmsTemplate的send/MessageCreator()发送Map类型的消息并在Message中附加属性用于消息过滤.
    public void sendNotificationQueue(final String message, final String deviceToken, final String recipientId) {
        jmsTemplate.send(notifyQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("message", message);
                mapMessage.setString("deviceToken", deviceToken);
                mapMessage.setString("recipientId", recipientId);

                mapMessage.setStringProperty("objectType", "notification");

                return mapMessage;
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
