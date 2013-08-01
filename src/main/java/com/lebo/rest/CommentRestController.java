package com.lebo.rest;

import com.lebo.entity.Comment;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.CommentService;
import com.lebo.service.DuplicateException;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.CommentListParam;
import com.lebo.service.param.FileInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 评论接口。
 *
 * @author: Wei Liu
 * Date: 13-7-11
 * Time: AM8:28
 */
@Controller
@RequestMapping("/api/1/comments")
public class CommentRestController {
    private Logger logger = LoggerFactory.getLogger(CommentRestController.class);

    @Autowired
    private CommentService commentService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StatusService statusService;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@RequestParam(value = "video", required = false) MultipartFile video,
                         @RequestParam(value = "image", required = false) MultipartFile image,
                         @RequestParam(value = "text", required = false) String text,
                         @RequestParam(value = "postId", required = false) String postId,
                         @RequestParam(value = "replyCommentId", required = false) String replyCommentId) {
        try {
            if(StringUtils.isBlank(postId) && StringUtils.isBlank(replyCommentId)){
                return ErrorDto.badRequest("参数postId和replyCommentId不能都为空");
            }

            Comment comment = new Comment();

            //回复Comment
            if(StringUtils.isNotBlank(replyCommentId)){
                Comment replyComment = commentService.getComment(replyCommentId);
                if(replyComment == null){
                    return ErrorDto.badRequest(String.format("replyCommentId[%s]无效", replyCommentId));
                }
                comment.setReplyCommentId(replyCommentId);
                comment.setReplyCommentUserId(replyComment.getUserId());
                comment.setPostId(replyComment.getPostId());
            //回复Post
            } else {
                if(!statusService.isPostExists(postId)){
                    return ErrorDto.badRequest(String.format("postId[%s]不存在", postId));
                }
                comment.setPostId(postId);
            }

            //文件
            List<FileInfo> fileInfos = new ArrayList<FileInfo>();
            if (video != null && image != null) {
                if (video.getSize() > StatusRestController.ONE_M_BYTE || image.getSize() > StatusRestController.ONE_M_BYTE) {
                    return ErrorDto.badRequest(" Upload Single file size cannot be greater than 1 M byte.");
                }
                fileInfos.add(new FileInfo(video.getInputStream(), video.getOriginalFilename(), video.getContentType()));
                fileInfos.add(new FileInfo(image.getInputStream(), image.getOriginalFilename(), image.getContentType()));
            }

            comment.setUserId(accountService.getCurrentUserId());
            comment.setText(text);
            comment.setCreatedAt(new Date());

            comment = commentService.create(comment, fileInfos);
            return commentService.toCommentDto(comment);

        } catch (DuplicateException e) {
            return ErrorDto.duplicate();
        } catch (Exception e) {
            logger.info("发布评论失败", e);
            return ErrorDto.internalServerError(e.getMessage());
        }
    }

    @RequestMapping(value = "show", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@Valid CommentListParam param) {
        return commentService.toCommentDtos(commentService.list(param));
    }
}
