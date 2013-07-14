package com.lebo.service;

import com.lebo.entity.Comment;
import com.lebo.repository.CommentDao;
import com.lebo.service.param.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-11
 * Time: AM8:31
 */
@Service
public class CommentService extends AbstractMongoService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private GridFsService gridFsService;
    @Autowired
    private StatusService statusService;

    /**
     * @throws com.mongodb.MongoException 当存储数据失败时
     * @throws DuplicateException 当文件重复时
     */
    public Comment create(String userId, String text, List<FileInfo> fileInfos, String postId) {
        List<String> fileIds = gridFsService.saveFilesSafely(fileInfos);

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setCreatedAt(new Date());
        comment.setText(text);
        comment.setMentions(statusService.mentionUserIds(text));
        comment.setFiles(fileIds);
        comment.setPostId(postId);
        comment = commentDao.save(comment);
        throwOnMongoError();

        return comment;
    }

    public int countPostComments(String postId) {
        return (int) mongoTemplate.count(new Query(new Criteria(Comment.POST_ID_KEY).is(postId)), Comment.class);
    }
}
