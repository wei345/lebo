package com.lebo.rest;

import com.lebo.entity.Comment;
import com.lebo.entity.FileInfo;
import com.lebo.entity.Post;
import com.lebo.entity.Setting;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.ALiYunStorageService;
import com.lebo.service.CommentService;
import com.lebo.service.DuplicateException;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.CommentListParam;
import com.lebo.web.ControllerUtils;
import org.apache.commons.io.FileUtils;
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

/**
 * 评论接口。
 *
 * @author: Wei Liu
 * Date: 13-7-11
 * Time: AM8:28
 */
@Controller
public class CommentRestController {
    private Logger logger = LoggerFactory.getLogger(CommentRestController.class);

    @Autowired
    private CommentService commentService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private ALiYunStorageService aLiYunStorageService;

    public static final String PREFIX_API_1_COMMENTS = "/api/1/comments/";
    public static final String PREFIX_API_1_1_COMMENTS = "/api/1.1/comments/";

    /**
     * 文字评论
     */
    //TODO 优化代码，视频评论和文字评论大部分重复
    @RequestMapping(value = PREFIX_API_1_COMMENTS + "create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@RequestParam(value = "text") String text,
                         @RequestParam(value = "postId", required = false) String postId,
                         @RequestParam(value = "replyCommentId", required = false) String replyCommentId) {

        try {
            logger.debug("正在发布文字评论:");
            logger.debug("            text : {}", text);
            logger.debug("          postId : {}", postId);
            logger.debug("  replyCommentId : {}", replyCommentId);

            if (StringUtils.isBlank(postId) && StringUtils.isBlank(replyCommentId)) {
                return ErrorDto.badRequest("参数postId和replyCommentId不能都为空");
            }

            Comment comment = new Comment();

            //回复Comment
            if (StringUtils.isNotBlank(replyCommentId)) {
                Comment replyComment = commentService.getComment(replyCommentId);
                if (replyComment == null) {
                    return ErrorDto.badRequest(String.format("replyCommentId[%s]无效", replyCommentId));
                }
                comment.setReplyCommentId(replyCommentId);
                comment.setReplyCommentUserId(replyComment.getUserId());
                comment.setPostId(replyComment.getPostId());
                //TODO 存被回复者screenName以提高性能
            }
            //回复Post
            else {
                Post post = statusService.getPost(postId);

                if (post == null) {
                    return ErrorDto.badRequest(String.format("postId[%s]不存在", postId));
                }

                if (post.getOriginPostId() == null) {
                    comment.setPostId(postId);
                } else {
                    comment.setPostId(post.getOriginPostId());
                }
            }

            comment.setUserId(accountService.getCurrentUserId());
            comment.setText(text);

            comment = commentService.create(comment, null, null, null);
            return commentService.toCommentDto(comment);

        } catch (DuplicateException e) {
            return ErrorDto.duplicate();
        } catch (Exception e) {
            logger.info("发布评论失败", e);
            return ErrorDto.internalServerError(e.getMessage());
        }
    }

    /**
     * 视频评论
     */
    @RequestMapping(value = PREFIX_API_1_COMMENTS + "createWithMedia", method = RequestMethod.POST)
    @ResponseBody
    public Object createWithMedia(@RequestParam(value = "video", required = false) MultipartFile video,
                                  @RequestParam(value = "image", required = false) MultipartFile image,
                                  @RequestParam(value = "audio", required = false) MultipartFile audio,
                                  @RequestParam(value = "audioDuration", required = false) Long audioDuration,
                                  @RequestParam(value = "text", required = false) String text,
                                  @RequestParam(value = "postId", required = false) String postId,
                                  @RequestParam(value = "replyCommentId", required = false) String replyCommentId) {
        try {
            logger.debug("正在发布视频评论:");
            if (video != null) {
                logger.debug("           video : {}", FileUtils.byteCountToDisplaySize(video.getSize()));
            }
            if (image != null) {
                logger.debug("           image : {}", FileUtils.byteCountToDisplaySize(image.getSize()));
            }
            if (audio != null) {
                logger.debug("           audio : {}", FileUtils.byteCountToDisplaySize(audio.getSize()));
            }
            logger.debug("            text : {}", text);
            logger.debug("          postId : {}", postId);
            logger.debug("  replyCommentId : {}", replyCommentId);

            //参数检查
            if (StringUtils.isBlank(postId) && StringUtils.isBlank(replyCommentId)) {
                return ErrorDto.badRequest("参数postId和replyCommentId不能都为空");
            }

            //文件大小限制
            if (video != null && video.getSize() > Setting.MAX_VIDEO_LENGTH_BYTES ||
                    image != null && image.getSize() > Setting.MAX_IMAGE_LENGTH_BYTES ||
                    audio != null && audio.getSize() > Setting.MAX_AUDIO_LENGTH_BYTES) {
                return ErrorDto.badRequest("上传的文件太大");
            }

            FileInfo videoFileInfo = null;
            FileInfo imageFileInfo = null;
            FileInfo audioFileInfo = null;

            //视频评论
            if (video != null && image != null) {
                videoFileInfo = ControllerUtils.getFileInfo(video);
                imageFileInfo = ControllerUtils.getFileInfo(image);
            }
            //语音评论
            else if (audio != null) {
                audioFileInfo = ControllerUtils.getFileInfo(audio);
                audioFileInfo.setDuration(audioDuration);
            }
            //参数错误
            else {
                return ErrorDto.badRequest("参数必须满足这个条件：(video != null && image != null) || (audio != null)");
            }

            return createComment(text, replyCommentId, postId, videoFileInfo, imageFileInfo, audioFileInfo);

        } catch (DuplicateException e) {
            return ErrorDto.duplicate();
        } catch (Exception e) {
            logger.info("发布评论失败", e);
            return ErrorDto.internalServerError(e.getMessage());
        }
    }

    @RequestMapping(value = PREFIX_API_1_COMMENTS + "show", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@Valid CommentListParam param) {
        return commentService.toCommentDtos(commentService.list(param));
    }

    @RequestMapping(value = PREFIX_API_1_COMMENTS + "destroy", method = RequestMethod.POST)
    @ResponseBody
    public Object destroy(@RequestParam(value = "id") String id) {
        Comment comment = commentService.getComment(id);
        if (comment == null) {
            return ErrorDto.badRequest(String.format("评论不存在(id=%s)", id));
        }

        boolean permission = false;
        //评论作者有权限删除
        if (comment.getUserId().equals(accountService.getCurrentUserId())) {
            permission = true;
        }

        //帖子作者有权限删除
        if (!permission) {
            Post post = statusService.getPost(comment.getPostId());
            if (accountService.getCurrentUserId().equals(post.getUserId())) {
                permission = true;
            }
        }

        if (permission) {
            commentService.deleteComment(comment);
            return commentService.toBasicCommentDto(comment);
        } else {
            return ErrorDto.forbidden();
        }
    }

    //-- v1.1 --//
    @RequestMapping(value = PREFIX_API_1_1_COMMENTS + "createWithMedia.json", method = RequestMethod.POST)
    @ResponseBody
    public Object createWithMedia_v1_1(@RequestParam(value = "videoUrl", required = false) String videoUrl,
                                       @RequestParam(value = "imageUrl", required = false) String imageUrl,
                                       @RequestParam(value = "audioUrl", required = false) String audioUrl,
                                       @RequestParam(value = "audioDuration", required = false) Long audioDuration,
                                       @RequestParam(value = "text", required = false) String text,
                                       @RequestParam(value = "postId", required = false) String postId,
                                       @RequestParam(value = "replyCommentId", required = false) String replyCommentId) {

        if (StringUtils.isBlank(postId) && StringUtils.isBlank(replyCommentId)) {
            return ErrorDto.badRequest("参数postId和replyCommentId不能都为空");
        }

        //读取附件信息
        FileInfo videoFileInfo = null;
        FileInfo imageFileInfo = null;
        FileInfo audioFileInfo = null;
        //视频评论
        if (StringUtils.isNotBlank(videoUrl) && StringUtils.isNotBlank(imageUrl)) {
            videoFileInfo = aLiYunStorageService.getTmpFileInfoFromUrl(videoUrl);
            imageFileInfo = aLiYunStorageService.getTmpFileInfoFromUrl(imageUrl);
        }
        //语音评论
        else if (StringUtils.isNotBlank(audioUrl)) {
            audioFileInfo = aLiYunStorageService.getTmpFileInfoFromUrl(audioUrl);
            audioFileInfo.setDuration(audioDuration);
        }
        //参数错误
        else {
            return ErrorDto.badRequest("参数必须满足这个条件：(video不为空 && image不为空) || (audio不为空)");
        }

        //附件大小限制
        if (videoFileInfo != null && videoFileInfo.getLength() > Setting.MAX_VIDEO_LENGTH_BYTES ||
                imageFileInfo != null && imageFileInfo.getLength() > Setting.MAX_IMAGE_LENGTH_BYTES ||
                audioFileInfo != null && audioFileInfo.getLength() > Setting.MAX_AUDIO_LENGTH_BYTES) {
            return ErrorDto.badRequest("上传的文件太大");
        }

        return createComment(text, replyCommentId, postId, videoFileInfo, imageFileInfo, audioFileInfo);
    }

    private Object createComment(String text, String replyCommentId, String postId,
                                 FileInfo videoFileInfo, FileInfo imageFileInfo, FileInfo audioFileInfo) {

        Comment comment = new Comment();
        comment.setUserId(accountService.getCurrentUserId());
        comment.setText(text);
        //回复Comment
        if (StringUtils.isNotBlank(replyCommentId)) {
            Comment replyComment = commentService.getComment(replyCommentId);
            if (replyComment == null) {
                return ErrorDto.badRequest(String.format("replyCommentId[%s]无效", replyCommentId));
            }
            comment.setReplyCommentId(replyCommentId);
            comment.setReplyCommentUserId(replyComment.getUserId());
            comment.setPostId(replyComment.getPostId());
        }
        //回复Post
        else {
            Post post = statusService.getPost(postId);
            if (post == null) {
                return ErrorDto.badRequest(String.format("postId[%s]不存在", postId));
            }
            //回复原始贴
            if (post.getOriginPostId() == null) {
                comment.setPostId(postId);
            }
            //回复转发贴，归结到原始贴上
            else {
                comment.setPostId(post.getOriginPostId());
            }
        }

        comment = commentService.create(comment, videoFileInfo, imageFileInfo, audioFileInfo);
        return commentService.toCommentDto(comment);
    }

}
