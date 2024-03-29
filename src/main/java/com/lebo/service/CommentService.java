package com.lebo.service;

import com.lebo.entity.Comment;
import com.lebo.entity.FileInfo;
import com.lebo.entity.Post;
import com.lebo.entity.User;
import com.lebo.event.AfterCommentCreateEvent;
import com.lebo.event.AfterCommentDestroyEvent;
import com.lebo.event.ApplicationEventBus;
import com.lebo.repository.CommentDao;
import com.lebo.rest.dto.CommentDto;
import com.lebo.rest.dto.UserDto;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.CommentListParam;
import com.lebo.service.param.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
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
    private FileStorageService fileStorageService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ApplicationEventBus eventBus;
    public static final String FILE_COLLECTION_NAME = "comment";

    /**
     * @throws com.mongodb.MongoException 当存储数据失败时
     * @throws DuplicateException         当文件重复时
     */
    public Comment create(Comment comment, FileInfo video, FileInfo videoFirstFrame, FileInfo audio) {
        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(new Date());
        }
        comment.setId(newMongoId(comment.getCreatedAt()));
        comment.setUserMentions(statusService.findUserMentions(comment.getText()));
        comment.setMentionUserIds(statusService.mentionUserIds(comment.getUserMentions()));

        //视频评论
        comment.setHasVideo(false);
        if (video != null && videoFirstFrame != null) {
            //设置唯一的文件名
            video.setKey(generateFileId(FILE_COLLECTION_NAME, comment.getId(), "video", video.getLength(), video.getContentType(), video.getFilename()));
            videoFirstFrame.setKey(generateFileId(FILE_COLLECTION_NAME, comment.getId(), "video-first-frame", videoFirstFrame.getLength(), videoFirstFrame.getContentType(), videoFirstFrame.getFilename()));
            //保存文件
            fileStorageService.save(video, videoFirstFrame);

            comment.setVideo(video);
            comment.setVideoFirstFrame(videoFirstFrame);
            comment.setHasVideo(true);
        }

        //语音评论
        if (audio != null) {
            audio.setKey(generateFileId(FILE_COLLECTION_NAME, comment.getId(), "audio", audio.getLength(), audio.getContentType(), audio.getFilename()));
            fileStorageService.save(audio);
            comment.setAudio(audio);
        }

        comment = commentDao.save(comment);
        throwOnMongoError();
        eventBus.post(new AfterCommentCreateEvent(comment));

        return comment;
    }

    public int countPostComments(String postId) {
        return (int) mongoTemplate.count(new Query(new Criteria(Comment.POST_ID_KEY).is(postId)), Comment.class);
    }

    public List<Comment> list(CommentListParam param) {
        Query query = new Query();
        if (StringUtils.isNotBlank(param.getPostId())) {
            query.addCriteria(new Criteria(Comment.POST_ID_KEY).is(param.getPostId()));
        }
        if (param.getHasVideo() != null) {
            query.addCriteria(new Criteria(Comment.HAS_VIDEO_KEY).is(param.getHasVideo()));
        }
        paginationById(query, param);
        return mongoTemplate.find(query, Comment.class);
    }

    public CommentDto toBasicCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPostId());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setText(comment.getText());
        if (comment.getVideo() != null) {
            dto.setVideo(comment.getVideo().toDto());
        }
        if (comment.getVideoConverted() != null) {
            dto.setVideoConverted(comment.getVideoConverted().toDto());
        }
        dto.setVideoFirstFrameUrl(comment.getVideoFirstFrameUrl());
        if (comment.getAudio() != null) {
            dto.setAudio(comment.getAudio().toDto());
        }
        dto.setHasVideo(comment.getHasVideo());
        dto.setReplyCommentId(comment.getReplyCommentId());

        //评论的作者信息
        User user = accountService.getUser(comment.getUserId());
        dto.setUser(accountService.toBasicUserDto(user));

        //回复评论的作者信息
        if (StringUtils.isNotBlank(comment.getReplyCommentUserId())) {
            User replyCommentUser = accountService.getUser(comment.getReplyCommentUserId());
            UserDto replyCommentUserDto = new UserDto();
            replyCommentUserDto.setScreenName(replyCommentUser.getScreenName());
            replyCommentUserDto.setProfileImageUrl(user.getProfileImageUrl());
            replyCommentUserDto.setId(replyCommentUser.getId());
            dto.setReplyCommentUser(replyCommentUserDto);
        }
        return dto;
    }

    public List<CommentDto> toBasicCommentDtos(List<Comment> comments) {
        List<CommentDto> dtos = new ArrayList<CommentDto>(comments.size());
        for (Comment comment : comments) {
            dtos.add(toBasicCommentDto(comment));
        }
        return dtos;
    }

    public CommentDto toCommentDto(Comment comment) {
        CommentDto dto = toBasicCommentDto(comment);
        accountService.dtoSetFollowing(dto.getUser());
        return dto;
    }

    public List<CommentDto> toCommentDtos(List<Comment> comments) {
        List<CommentDto> dtos = new ArrayList<CommentDto>(comments.size());
        for (Comment comment : comments) {
            dtos.add(toCommentDto(comment));
        }
        return dtos;
    }

    public void deleteByPostId(String postId) {
        List<Comment> comments = mongoTemplate.find(new Query(new Criteria(Comment.POST_ID_KEY).is(postId)),
                Comment.class);
        for (Comment comment : comments) {
            deleteComment(comment);
        }
    }

    public Comment getComment(String id) {
        return commentDao.findOne(id);
    }

    public void deleteComment(Comment comment) {
        Assert.notNull(comment);

        if (comment.getVideo() != null) {
            fileStorageService.delete(comment.getVideo().getKey());
        }
        if (comment.getVideoFirstFrame() != null) {
            fileStorageService.delete(comment.getVideoFirstFrame().getKey());
        }
        if (comment.getAudio() != null) {
            fileStorageService.delete(comment.getAudio().getKey());
        }
        commentDao.delete(comment);
        eventBus.post(new AfterCommentDestroyEvent(comment));
    }

    //---- 后台管理 ----//

    public Page<Comment> list(String userId,
                              String postId,
                              PageRequest pageRequest) {

        Query query = new Query();
        if (StringUtils.isNotBlank(postId)) {
            query.addCriteria(new Criteria(Comment.POST_ID_KEY).is(postId));
        }
        if (StringUtils.isNotBlank(userId)) {
            query.addCriteria(new Criteria(Comment.USER_ID_KEY).is(userId));
        }

        query.with(pageRequest);

        Page<Comment> page = new PageImpl<Comment>(mongoTemplate.find(query, Comment.class),
                pageRequest,
                mongoTemplate.count(query, Comment.class));

        //查找
        return page;
    }
}
