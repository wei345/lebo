package com.lebo.service;

import com.lebo.entity.FileInfo;
import com.lebo.entity.Message;
import com.lebo.repository.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Wei Liu
 * Date: 13-7-14
 * Time: PM8:00
 */
@Service
public class MessageService extends AbstractMongoService {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private MessageDao messageDao;

    public Message newMessage(String from, String to, String text, FileInfo video, FileInfo videoFirstFrame, String source) {
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText(text);
        if (video != null && videoFirstFrame != null) {
            fileStorageService.save(video, videoFirstFrame);
            message.setVideo(video);
            message.setVideoFirstFrame(videoFirstFrame);
        }
        message.setCreatedAt(dateProvider.getDate());
        message.setSource(source);
        messageDao.save(message);
        throwOnMongoError();

        return message;
    }
}
