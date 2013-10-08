package com.lebo.jms;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * 转换帖子中的视频.
 *
 * @author: Wei Liu
 * Date: 13-10-8
 * Time: PM2:21
 */
public class ConvertVideoMessageProducer {
    private JmsTemplate jmsTemplate;
    private Destination convertVideoQueue;

    public static final String OBJECT_TYPE_POST = "post";
    public static final String OBJECT_TYPE_COMMENT = "comment";

    public void sendQueue(final String objectType, final String objectId) {
        jmsTemplate.send(convertVideoQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("objectId", objectId);

                mapMessage.setStringProperty("objectType", objectType);

                return mapMessage;
            }
        });
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setConvertVideoQueue(Destination convertVideoQueue) {
        this.convertVideoQueue = convertVideoQueue;
    }
}
