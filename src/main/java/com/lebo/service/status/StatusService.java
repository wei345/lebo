package com.lebo.service.status;

import com.lebo.entity.FsFiles;
import com.lebo.entity.Status;
import com.lebo.entity.User;
import com.lebo.repository.FsFilesDao;
import com.lebo.repository.StatusDao;
import com.mongodb.CommandResult;
import com.mongodb.gridfs.GridFSFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import java.io.*;
import java.util.Date;

/**
 * @author: Wei Liu
 * Date: 13-7-2
 * Time: PM4:32
 */
@Service
public class StatusService {

    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private StatusDao statusDao;
    @Autowired
    private FsFilesDao fsFilesDao;

    public Status update(String userId, String text, File media) throws IOException {
        String fileId;
        String md5 = Encodes.encodeHex(Digests.md5(new FileInputStream(media)));
        FsFiles fsFiles = fsFilesDao.findByMd5(md5);

        if (fsFiles == null) {
            String mediaType = StringUtils.substringAfterLast(media.getName(), ".");
            GridFSFile gridFSFile = gridFsTemplate.store(new FileInputStream(media), media.getName(), mediaType);
            fileId = gridFSFile.getId().toString();
        } else {
            fileId = fsFiles.getId().toString();
        }

        Status status = new Status();
        status.setUser(new User(userId));
        status.setCreatedAt(new Date());
        status.setText(text);
        status.setMedia(new FsFiles(fileId));

        status = statusDao.save(status);

        CommandResult commandResult = mongoTemplate.executeCommand("{ getLastError : 1 }");
        Object code = commandResult.get("code");
        if (code != null) {
            throw new RuntimeException(commandResult.get("err").toString());
        }
        return status;
    }
}
