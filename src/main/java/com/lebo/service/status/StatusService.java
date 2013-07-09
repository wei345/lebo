package com.lebo.service.status;

import com.google.common.collect.Lists;
import com.lebo.entity.Tweet;
import com.lebo.repository.TweetDao;
import com.lebo.service.GridFsService;
import com.lebo.service.MongoService;
import com.lebo.service.TimelineParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-2
 * Time: PM4:32
 */
@Service
public class StatusService extends MongoService {

    @Autowired
    private TweetDao tweetDao;
    @Autowired
    private GridFsService gridFsService;

    public Tweet update(String userId, String text, List<File> files) throws IOException {
        List<String> fileIds = Lists.newArrayList();
        for (File file : files) {
            fileIds.add(gridFsService.save(file.content, file.filename, file.contentType));
        }

        Tweet tweet = new Tweet();
        tweet.setUserId(userId);
        tweet.setCreatedAt(new Date());
        tweet.setText(text);
        tweet.setFiles(fileIds);
        tweet = tweetDao.save(tweet);
        throwOnMongoError();

        for (String id : fileIds) {
            gridFsService.increaseReferrerCount(id);
        }
        return tweet;
    }

    public List<Tweet> userTimeline(TimelineParam param) {
        Assert.hasText(param.getUserId(), "The userId can not be null");

        if (param.canIgnoreIdCondition()) {
            return tweetDao.findByUserId(param.getUserId(), param).getContent();
        } else {
            return tweetDao.userTimeline(param.getUserId(), param.getMaxId(), param.getSinceId(), param).getContent();
        }
    }

    /**
     * 表示一个要保存的文件
     */
    public static class File {
        InputStream content;
        String filename;
        String contentType;
        Long size;

        public File(InputStream content, String filename, String contentType) {
            this.content = content;
            this.filename = filename;
            this.contentType = contentType;
        }
    }
}
