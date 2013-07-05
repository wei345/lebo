package com.lebo.service.status;

import com.lebo.entity.FsFiles;
import com.lebo.entity.Tweet;
import com.lebo.entity.User;
import com.lebo.repository.FsFilesDao;
import com.lebo.repository.TweetDao;
import com.lebo.service.MongoService;
import com.mongodb.gridfs.GridFSFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    public Tweet update(String userId, String text, File media) throws IOException {
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
