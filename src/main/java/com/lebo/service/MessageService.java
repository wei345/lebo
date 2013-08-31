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
    private static final String FILE_COLLECTION_NAME = "message";

    public Message newMessage(String from, String to, String text, FileInfo video, FileInfo videoFirstFrame, String source) {
        Message message = new Message();

        message.setCreatedAt(dateProvider.getDate());
        message.setId(newMongoId(message.getCreatedAt()));
        message.setFrom(from);
        message.setTo(to);
        message.setText(text);
        message.setSource(source);

        if (video != null && videoFirstFrame != null) {
            video.setKey(generateFileId(FILE_COLLECTION_NAME, message.getId(), video.getLength(), video.getContentType(), video.getFilename()));
            videoFirstFrame.setKey(generateFileId(FILE_COLLECTION_NAME, message.getId(), videoFirstFrame.getLength(), videoFirstFrame.getContentType(), videoFirstFrame.getFilename()));

            fileStorageService.save(video, videoFirstFrame);
            message.setVideo(video);
            message.setVideoFirstFrame(videoFirstFrame);
        }

        messageDao.save(message);
        throwOnMongoError();
        return message;
    }

}
