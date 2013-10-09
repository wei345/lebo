package com.lebo.service;

import com.lebo.entity.Comment;
import com.lebo.entity.Post;
import com.lebo.jms.ConvertVideoMessageProducer;
import com.mongodb.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * @author: Wei Liu
 * Date: 13-10-9
 * Time: PM5:31
 */
@Service
public class FeedbackService extends AbstractMongoService {
    private Logger logger = LoggerFactory.getLogger(FeedbackService.class);

    @Autowired
    private ConvertVideoMessageProducer convertVideoMessageProducer;

    /**
     * 将帖子或视频评论添加到转码队列
     */
    public void addVideoConvertQueue(String objectType, String objectId) {
        //将帖子加入转码队列
        if (VideoConvertService.OBJECT_TYPE_POST.equals(objectType)) {
            Query query = new Query();
            query.addCriteria(new Criteria(Post.ID_KEY).is(objectId));
            query.addCriteria(new Criteria(Post.VIDEO_CONVERT_STATUS_KEY).is(VideoConvertService.VIDEO_CONVERT_STATUS_UNCONVERT));
            WriteResult result = mongoTemplate.updateFirst(query,
                    new Update().set(Post.VIDEO_CONVERT_STATUS_KEY, VideoConvertService.VIDEO_CONVERT_STATUS_ADDED_QUEUE),
                    Post.class);
            if (result.getN() == 1) {
                logger.debug("{} 正在添加到任务队列, objectType : {}, objectId : {}", VideoConvertService.VIDEO_CONVERT_LOG_PREFIX, objectType, objectId);
                convertVideoMessageProducer.sendQueue(VideoConvertService.OBJECT_TYPE_POST, objectId);
            } else {
                logger.debug("{} 不添加任务队列，因为正在转换或已转换, objectType : {}, objectId : {}", VideoConvertService.VIDEO_CONVERT_LOG_PREFIX, objectType, objectId);
            }
        }

        //将视频评论加入转码队列
        if (VideoConvertService.OBJECT_TYPE_COMMENT.equals(objectType)) {
            Query query = new Query();
            query.addCriteria(new Criteria(Comment.ID_KEY).is(objectId));
            query.addCriteria(new Criteria(Comment.VIDEO_CONVERT_STATUS_KEY).is(VideoConvertService.VIDEO_CONVERT_STATUS_UNCONVERT));
            WriteResult result = mongoTemplate.updateFirst(query,
                    new Update().set(Comment.VIDEO_CONVERT_STATUS_KEY, VideoConvertService.VIDEO_CONVERT_STATUS_ADDED_QUEUE),
                    Comment.class);
            if (result.getN() == 1) {
                logger.debug("{} 正在添加到任务队列, objectType : {}, objectId : {}", VideoConvertService.VIDEO_CONVERT_LOG_PREFIX, objectType, objectId);
                convertVideoMessageProducer.sendQueue(VideoConvertService.OBJECT_TYPE_COMMENT, objectId);
            } else {
                logger.debug("{} 不添加任务队列，因为正在转换或已转换, objectType : {}, objectId : {}", VideoConvertService.VIDEO_CONVERT_LOG_PREFIX, objectType, objectId);
            }
        }
    }

}
