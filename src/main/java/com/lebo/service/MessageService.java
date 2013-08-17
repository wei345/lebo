package com.lebo.service;

import com.lebo.entity.Message;
import com.lebo.repository.MessageDao;
import com.lebo.service.param.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Message newMessage(String from, String to, String text, List<FileInfo> fileInfos, String source) {
        List<String> fileIds = fileStorageService.saveFiles(fileInfos);

        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText(text);
        message.setFiles(fileIds);
        message.setCreatedAt(dateProvider.getDate());
        message.setSource(source);
        messageDao.save(message);
        throwOnMongoError();

        return message;
    }
}
