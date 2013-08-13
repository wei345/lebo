package com.lebo.jms;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * @author: Wei Liu
 * Date: 13-8-13
 * Time: AM11:23
 */
public class SettingMessageProducer {
    private JmsTemplate jmsTemplate;
    private Destination settingTopic;

    public void sendTopic() {
        jmsTemplate.send(settingTopic, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                MapMessage message = session.createMapMessage();
                message.setStringProperty("objectType", "setting");

                return message;
            }
        });
    }


    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setSettingTopic(Destination settingTopic) {
        this.settingTopic = settingTopic;
    }
}
