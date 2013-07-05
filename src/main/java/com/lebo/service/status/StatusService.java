package com.lebo.service.status;

import com.lebo.entity.FsFiles;
import com.lebo.entity.Tweet;
import com.lebo.entity.User;
import com.lebo.repository.FsFilesDao;
import com.lebo.repository.TweetDao;
import com.lebo.service.MongoService;
import com.mongodb.gridfs.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StatusService extends MongoService {

    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private TweetDao tweetDao;
    @Autowired
    private FsFilesDao fsFilesDao;

    public Tweet update(String userId, String text, InputStream media, Long mediaSizeInByte, String mediaName) throws IOException {
        if(!media.markSupported()){
            media = new ByteArrayInputStream(IOUtils.toByteArray(media));
        }

        String fileId;
        media.mark(Integer.MAX_VALUE);
        String md5 = Encodes.encodeHex(Digests.md5(media));
        FsFiles fsFiles = fsFilesDao.findByMd5(md5);

        if (fsFiles == null) {
            String mediaType = StringUtils.substringAfterLast(mediaName, ".");
            media.reset();
            GridFSFile gridFSFile = gridFsTemplate.store(media, mediaName, mediaType);
            fileId = gridFSFile.getId().toString();
            checkMongoError();
        } else {
            fileId = fsFiles.getId().toString();
        }

        Tweet tweet = new Tweet();
        tweet.setUser(new User(userId));
        tweet.setCreatedAt(new Date());
        tweet.setText(text);
        tweet.setMedia(new FsFiles(fileId));

        tweet = tweetDao.save(tweet);

        checkMongoError();

        return tweet;
    }
}
