package com.lebo.jms;

import com.lebo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * JMS用户变更消息生产者.
 * <p/>
 * 使用jmsTemplate将用户变更消息分别发送到queue与topic.
 *
 * @author Wei Liu
 */
public class ApnsMessageProducer {

    private Logger logger = LoggerFactory.getLogger(ApnsMessageProducer.class);

    private JmsTemplate jmsTemplate;
    private Destination notifyQueue;

    /**
     * @param message     ios通知内容
     * @param deviceToken ios设备token
     * @param recipient
     */
    //使用jmsTemplate的send/MessageCreator()发送Map类型的消息并在Message中附加属性用于消息过滤.
    public void sendNotificationQueue(final String message, final String deviceToken, final User recipient) {
        jmsTemplate.send(notifyQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("message", message);
                mapMessage.setString("deviceToken", deviceToken);
                mapMessage.setString("recipientId", recipient.getId());
                mapMessage.setString("recipientScreenName", recipient.getScreenName());

                mapMessage.setStringProperty("objectType", "notification");

                return mapMessage;
            }
        });

        logger.debug("添加到APNS通知队列: recipient : {}({}), message : {}, deviceToken : {}",
                recipient.getScreenName(), recipient.getId(), message, deviceToken);
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setNotifyQueue(Destination notifyQueue) {
        this.notifyQueue = notifyQueue;
    }
}
