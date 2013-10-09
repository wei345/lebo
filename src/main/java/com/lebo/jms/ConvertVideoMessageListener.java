package com.lebo.jms;

import com.lebo.service.VideoConvertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 转换帖子中的视频.
 * 解决ios发布视频android不能播放问题。
 *
 * @author: Wei Liu
 * Date: 13-10-8
 * Time: PM2:22
 */
public class ConvertVideoMessageListener implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(ConvertVideoMessageListener.class);
    @Autowired
    private VideoConvertService videoConvertService;

    @Override
    public void onMessage(Message message) {
        String objectType = null;
        String objectId = null;
        try {
            MapMessage mapMessage = (MapMessage) message;
            objectType = mapMessage.getStringProperty("objectType");
            objectId = mapMessage.getString("objectId");

            //帖子
            if (VideoConvertService.OBJECT_TYPE_POST.equals(objectType)) {
                videoConvertService.converPostVideo(objectId);
            }

            //视频评论
            if (VideoConvertService.OBJECT_TYPE_COMMENT.equals(objectType)) {
                videoConvertService.convertCommentVideo(objectId);
            }
        } catch (Exception e) {
            logger.error(String.format("视频转码 : 发生错误 objectType : %s, objectId : %s", objectType, objectId), e);
        }
    }


}
