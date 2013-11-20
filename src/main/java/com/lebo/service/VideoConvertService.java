package com.lebo.service;

import com.lebo.entity.Comment;
import com.lebo.entity.FileInfo;
import com.lebo.entity.Post;
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
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 视频转码，解决部分ios发的视频android不能播放的问题
 * 如果ios和android客户端能做到使用相同的编码录制和播放视频，服务端就不用做编码转换了
 *
 * @author: Wei Liu
 * Date: 13-10-9
 * Time: PM6:56
 */
@Service
public class VideoConvertService {
    private Logger logger = LoggerFactory.getLogger(VideoConvertService.class);
    public static final String VIDEO_CONVERT_STATUS_UNCONVERT = null;
    public static final String VIDEO_CONVERT_STATUS_ADDED_QUEUE = "ADDED_QUEUE";
    public static final String VIDEO_CONVERT_STATUS_CONVERTING = "CONVERTING";
    public static final String VIDEO_CONVERT_STATUS_CONVERTED = "CONVERTED";
    public static final String SLUG_VIDEO = "video-converted";
    public static final String VIDEO_CONVERT_LOG_PREFIX = "视频转码 :";
    public static final String OBJECT_TYPE_POST = "post";
    public static final String OBJECT_TYPE_COMMENT = "comment";

    @Autowired
    private StatusService statusService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private MongoTemplate mongoTemplate;

    public void converPostVideo(String postId) throws IOException {
        Post post = statusService.getPost(postId);

        //获取原始帖
        if(post.getOriginPostId() != null){
            postId = post.getOriginPostId();
            post = statusService.getPost(post.getOriginPostId());
        }

        Assert.notNull(post);

        mongoTemplate.updateFirst(new Query(new Criteria(Post.ID_KEY).is(postId)),
                new Update().set(Post.VIDEO_CONVERT_STATUS_KEY, VIDEO_CONVERT_STATUS_CONVERTING), Post.class);

        if(post.getVideoConverted() != null){
            logger.debug("{} 已转码，中止, postId : {}", VIDEO_CONVERT_LOG_PREFIX, postId);
            return;
        }

        FileInfo oldVideo = post.getVideo();
        String oldVideoUrl = oldVideo.getContentUrl();
        File tmpFileNewVideo = File.createTempFile("video-convert-", ".mp4");

        //转码
        long begin = System.currentTimeMillis();
        logger.debug("{} 开始 {}", VIDEO_CONVERT_LOG_PREFIX, oldVideoUrl);
        File tmpFileOldVideo = fetchUrlForFile(oldVideoUrl);
        VideoUtils.convertVideo(tmpFileOldVideo.getAbsolutePath(), tmpFileNewVideo.getAbsolutePath());

        //保存新视频
        FileInfo video = new FileInfo(new FileInputStream(tmpFileNewVideo), "video/mp4", tmpFileNewVideo.length(), tmpFileNewVideo.getName());
        video.setKey(statusService.generateFileId(StatusService.FILE_COLLECTION_NAME, post.getId(), SLUG_VIDEO, video.getLength(), video.getContentType(), video.getFilename()));
        fileStorageService.save(video);

        mongoTemplate.updateFirst(new Query(new Criteria(Post.ID_KEY).is(postId)), new Update().set(Post.VIDEO_CONVERTED_KEY, video), Post.class);

        mongoTemplate.updateFirst(new Query(new Criteria(Post.ID_KEY).is(postId)),
                new Update().set(Post.VIDEO_CONVERT_STATUS_KEY, VIDEO_CONVERT_STATUS_CONVERTED), Post.class);

        logger.debug("{} 完成 {} ms, {} ({}) -> {} ({})",
                VIDEO_CONVERT_LOG_PREFIX,
                System.currentTimeMillis() - begin,
                oldVideoUrl,
                FileUtils.byteCountToDisplaySize(tmpFileOldVideo.length()),
                video.getContentUrl(),
                FileUtils.byteCountToDisplaySize(tmpFileNewVideo.length()));
        tmpFileNewVideo.delete();
        tmpFileOldVideo.delete();
    }

    public void convertCommentVideo(String commentId) throws IOException {
        mongoTemplate.updateFirst(new Query(new Criteria(Comment.ID_KEY).is(commentId)),
                new Update().set(Comment.VIDEO_CONVERT_STATUS_KEY, VIDEO_CONVERT_STATUS_CONVERTING), Comment.class);

        Comment comment = commentService.getComment(commentId);
        FileInfo oldVideo = comment.getVideo();
        if (oldVideo == null) {
            logger.debug("{} 没有视频 objectType : {}, objectId : {}", VIDEO_CONVERT_LOG_PREFIX, OBJECT_TYPE_COMMENT, commentId);
            return;
        }
        if (comment.getVideoConverted() != null) {
            logger.debug("{} 已转码，中止, commentId : {}", VIDEO_CONVERT_LOG_PREFIX, commentId);
            return;
        }

        String oldVideoUrl = oldVideo.getContentUrl();
        File tmpFileNewVideo = File.createTempFile("video-convert-", ".mp4");

        //转码
        long begin = System.currentTimeMillis();
        logger.debug("{} 开始 {}", VIDEO_CONVERT_LOG_PREFIX, oldVideoUrl);
        File tmpFileOldVideo = fetchUrlForFile(oldVideoUrl);
        VideoUtils.convertVideo(tmpFileOldVideo.getAbsolutePath(), tmpFileNewVideo.getAbsolutePath());

        //保存新视频
        FileInfo video = new FileInfo(new FileInputStream(tmpFileNewVideo), "video/mp4", tmpFileNewVideo.length(), tmpFileNewVideo.getName());
        video.setKey(statusService.generateFileId(CommentService.FILE_COLLECTION_NAME, comment.getId(), SLUG_VIDEO, video.getLength(), video.getContentType(), video.getFilename()));
        fileStorageService.save(video);

        mongoTemplate.updateFirst(new Query(new Criteria(Comment.ID_KEY).is(commentId)), new Update().set(Comment.VIDEO_CONVERTED_KEY, video), Comment.class);

        mongoTemplate.updateFirst(new Query(new Criteria(Comment.ID_KEY).is(commentId)),
                new Update().set(Comment.VIDEO_CONVERT_STATUS_KEY, VIDEO_CONVERT_STATUS_CONVERTED), Comment.class);

        logger.debug("{} 完成 {} ms, {} ({}) -> {} ({})",
                VIDEO_CONVERT_LOG_PREFIX,
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
