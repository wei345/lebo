package com.lebo.jms;

import com.lebo.entity.Comment;
import com.lebo.entity.FileInfo;
import com.lebo.entity.Post;
import com.lebo.service.CommentService;
import com.lebo.service.FileStorageService;
import com.lebo.service.StatusService;
import com.lebo.util.VideoUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private StatusService statusService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void onMessage(Message message) {
        String objectType = null;
        String objectId = null;
        try {
            MapMessage mapMessage = (MapMessage) message;
            objectType = mapMessage.getStringProperty("objectType");
            objectId = mapMessage.getString("objectId");

            //帖子
            if (ConvertVideoMessageProducer.OBJECT_TYPE_POST.equals(objectType)) {
                converPostVideo(objectId);
            }

            //视频评论
            if (ConvertVideoMessageProducer.OBJECT_TYPE_COMMENT.equals(objectType)) {
                convertCommentVideo(objectId);
            }
        } catch (Exception e) {
            logger.error(String.format("视频转码 : 发生错误 objectType : %s, objectId : %s", objectType, objectId), e);
        }
    }

    void converPostVideo(String postId) throws IOException {
        Post post = statusService.getPost(postId);
        FileInfo oldVideo = post.getVideo();
        String oldVideoUrl = oldVideo.getContentUrl();
        File tmpFileNewVideo = File.createTempFile("convert-video-", ".mp4");

        //转码
        long begin = System.currentTimeMillis();
        logger.debug("视频转码 : 开始 {}", oldVideoUrl);
        File tmpFileOldVideo = fetchUrlForFile(oldVideoUrl);
        VideoUtils.convertVideo(tmpFileOldVideo.getAbsolutePath(), tmpFileNewVideo.getAbsolutePath());

        //保存新视频
        FileInfo video = new FileInfo(new FileInputStream(tmpFileNewVideo), "video/mp4", tmpFileNewVideo.length(), tmpFileNewVideo.getName());
        video.setKey(statusService.generateFileId(StatusService.FILE_COLLECTION_NAME, post.getId(), StatusService.SLUG_VIDEO, video.getLength(), video.getContentType(), video.getFilename()));
        fileStorageService.save(video);

        mongoTemplate.updateFirst(new Query(new Criteria(Post.ID_KEY).is(post.getId())), new Update().set(Post.VIDEO_KEY, video), Post.class);

        //删除旧视频
        if (!oldVideo.getKey().equals(video.getKey())) {
            fileStorageService.delete(oldVideo.getKey());
        }
        logger.debug("视频转码 : 完成 {} ms, {} ({}) -> {} ({})",
                System.currentTimeMillis() - begin,
                oldVideoUrl,
                FileUtils.byteCountToDisplaySize(tmpFileOldVideo.length()),
                video.getContentUrl(),
                FileUtils.byteCountToDisplaySize(tmpFileNewVideo.length()));
        tmpFileNewVideo.delete();
        tmpFileOldVideo.delete();
    }

    void convertCommentVideo(String commentId) throws IOException {
        Comment comment = commentService.getComment(commentId);
        FileInfo oldVideo = comment.getVideo();
        if (oldVideo == null) {
            logger.debug("视频转码 : 没有视频 objectType : {}, objectId : {}", ConvertVideoMessageProducer.OBJECT_TYPE_COMMENT, commentId);
            return;
        }
        String oldVideoUrl = oldVideo.getContentUrl();
        File tmpFileNewVideo = File.createTempFile("convert-video-", ".mp4");

        //转码
        long begin = System.currentTimeMillis();
        logger.debug("视频转码 : 开始 {}", oldVideoUrl);
        File tmpFileOldVideo = fetchUrlForFile(oldVideoUrl);
        VideoUtils.convertVideo(tmpFileOldVideo.getAbsolutePath(), tmpFileNewVideo.getAbsolutePath());

        //保存新视频
        FileInfo video = new FileInfo(new FileInputStream(tmpFileNewVideo), "video/mp4", tmpFileNewVideo.length(), tmpFileNewVideo.getName());
        video.setKey(statusService.generateFileId(CommentService.FILE_COLLECTION_NAME, comment.getId(), CommentService.SLUG_VIDEO, video.getLength(), video.getContentType(), video.getFilename()));
        fileStorageService.save(video);

        mongoTemplate.updateFirst(new Query(new Criteria(Comment.ID_KEY).is(comment.getId())), new Update().set(Comment.VIDEO_KEY, video), Comment.class);

        //删除旧视频
        if (!oldVideo.getKey().equals(video.getKey())) {
            fileStorageService.delete(oldVideo.getKey());
        }
        logger.debug("视频转码 : 完成 {} ms, {} ({}) -> {} ({})",
                System.currentTimeMillis() - begin,
                oldVideoUrl,
                FileUtils.byteCountToDisplaySize(tmpFileOldVideo.length()),
                video.getContentUrl(),
                FileUtils.byteCountToDisplaySize(tmpFileNewVideo.length()));
        tmpFileNewVideo.delete();
        tmpFileOldVideo.delete();
    }

    File fetchUrlForFile(String url) {
        HttpURLConnection conn = null;
        try {
            File tmpFile = File.createTempFile("video-", "." + StringUtils.substringAfterLast(url, "."));
            conn = (HttpURLConnection) new URL(url).openConnection();
            IOUtils.copy(conn.getInputStream(), new FileOutputStream(tmpFile));

            return tmpFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
