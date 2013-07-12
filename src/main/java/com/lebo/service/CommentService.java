package com.lebo.service;

import com.google.common.collect.Lists;
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
    private MentionService mentionService;

    public Comment create(String userId, String text, List<FileInfo> files, String postId) throws IOException {
        List<String> fileIds = Lists.newArrayList();
        for (FileInfo file : files) {
            fileIds.add(gridFsService.save(file.getContent(), file.getFilename(), file.getMimeType()));
        }

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setCreatedAt(new Date());
        comment.setText(text);
        comment.setMentions(mentionService.mentionUserIds(text));
        comment.setFiles(fileIds);
        comment.setPostId(postId);
        comment = commentDao.save(comment);
        throwOnMongoError();

        for (String id : fileIds) {
            gridFsService.increaseViewCount(id);
        }
        return comment;
    }

    public int countPostComments(String postId){
        return (int)mongoTemplate.count(new Query(new Criteria(Comment.POST_ID_KEY).is(postId)), Comment.class);
    }
}
