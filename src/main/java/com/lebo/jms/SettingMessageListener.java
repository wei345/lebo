package com.lebo.jms;

import com.lebo.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author: Wei Liu
 * Date: 13-8-13
 * Time: AM11:04
 */
public class SettingMessageListener implements MessageListener {
    private static Logger logger = LoggerFactory.getLogger(SettingMessageListener.class);
    @Autowired
    private SettingService settingService;

    @Override
    public void onMessage(Message message) {
        settingService.reloadSetting();
        logger.info("已重新加载setting");
    }
}
