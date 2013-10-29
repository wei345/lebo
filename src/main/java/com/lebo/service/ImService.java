package com.lebo.service;

import com.lebo.entity.FileInfo;
import com.lebo.entity.Im;
import com.lebo.repository.ImDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM1:17
 */
public class ImService extends AbstractMongoService {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ImDao imDao;
    public static final String FILE_COLLECTION_NAME = "im";

    //目前只保存附件
    public Im create(String fromUserId, String toUserId, List<FileInfo> attachments) {
        Im im = new Im();
        im.setCreatedAt(new Date());
        im.setId(newMongoId(im.getCreatedAt()));
        im.setFrom(fromUserId);
        im.setTo(toUserId);

        //保存附件
        for (int i = 0; i < attachments.size(); i++) {
            FileInfo fileInfo = attachments.get(i);
            fileInfo.setKey(
                    generateFileId(FILE_COLLECTION_NAME,
                            im.getId(),
                            i + "-" + fileInfo.getContentType().replace("/", "-"),
                            fileInfo.getLength(),
                            fileInfo.getContentType(),
                            fileInfo.getFilename()));
        }
        fileStorageService.save(attachments.toArray(new FileInfo[]{}));
        im.setAttachments(attachments);

        imDao.save(im);
        return im;
    }
}
