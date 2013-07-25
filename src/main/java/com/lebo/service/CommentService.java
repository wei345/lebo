package com.lebo.service;

import com.lebo.entity.Comment;
import com.lebo.entity.User;
import com.lebo.repository.CommentDao;
import com.lebo.rest.dto.CommentDto;
import com.lebo.rest.dto.StatusDto;
import com.lebo.rest.dto.UserDto;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.CommentShowParam;
import com.lebo.service.param.FileInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.BeanMapper;

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
    private GridFsService gridFsService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private AccountService accountService;

    /**
     * @throws com.mongodb.MongoException 当存储数据失败时
     * @throws DuplicateException         当文件重复时
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

    public List<Comment> show(CommentShowParam param) {
        return commentDao.findByPostId(param.getPostId(), param);
    }

    public CommentDto toCommentDto(Comment comment){
        CommentDto dto = BeanMapper.map(comment, CommentDto.class);

        List<StatusDto.FileInfoDto> fileInfoDtos = new ArrayList<StatusDto.FileInfoDto>(2);
        for (String fileId : comment.getFiles()) {
            fileInfoDtos.add(gridFsService.getFileInfoDto(fileId, null));
        }
        dto.setFiles(fileInfoDtos);

        //是否为视频回复
        for(StatusDto.FileInfoDto file : fileInfoDtos){
            if(StringUtils.startsWith(file.getContentType(), "video/")){
                dto.setHasVideo(true);
                break;
            }
        }

        //评论的作者信息
        User user = accountService.getUser(comment.getUserId());
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setScreenName(user.getScreenName());
        userDto.setProfileImageUrl(gridFsService.getContentUrl(user.getProfileImageUrl()));
        userDto.setDescription(user.getDescription());
        dto.setUser(userDto);

        return dto;
    }

    public List<CommentDto> toCommentDtos(List<Comment> comments){
        List<CommentDto> dtos = new ArrayList<CommentDto>(comments.size());
        for(Comment comment : comments){
            dtos.add(toCommentDto(comment));
        }
        return dtos;
    }
}
