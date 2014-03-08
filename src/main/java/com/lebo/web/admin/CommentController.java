package com.lebo.web.admin;

import com.lebo.entity.Comment;
import com.lebo.entity.Post;
import com.lebo.entity.User;
import com.lebo.service.CommentService;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PageRequest;
import com.lebo.web.ControllerSetup;
import com.lebo.web.ControllerUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理评论.
 *
 * @author: Wei Liu
 * Date: 13-9-28
 * Time: PM10:11
 */
@RequestMapping("/admin/comment")
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StatusService statusService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "postId", required = false) String postId,
                       @Valid PageRequest pageRequest,
                       Model model) {

        Page<Comment> page = commentService.list(userId, postId, pageRequest);

        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>(page.getNumber());
        for (Comment comment : page.getContent()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", comment.getId());

            if (comment.getVideo() != null) {
                map.put("mediaUrl", comment.getVideo().getContentUrl());
            }

            if (comment.getVideoFirstFrame() != null) {
                map.put("mediaLinkText", String.format("<img src='%s'/>", comment.getVideoFirstFrameUrl()));
            }

            if (comment.getAudio() != null) {
                map.put("mediaUrl", comment.getAudio().getContentUrl());
                map.put("mediaLinkText", "语音评论");
            }

            map.put("text", comment.getText());

            if (comment.getReplyCommentUserId() != null) {
                User user = accountService.getUser(comment.getReplyCommentUserId());
                if (user != null) {
                    map.put("replyTo", user.getScreenName());
                }
            }

            User user = accountService.getUser(comment.getUserId());
            if (user == null) {
                map.put("screenName", "<i>未知</i>");
            } else {
                map.put("screenName", user.getScreenName());
            }

            map.put("createdAt", ControllerSetup.DEFAULT_DATE_FORMAT.format(comment.getCreatedAt()));
            maps.add(map);
        }

        model.addAttribute("comments", maps);
        model.addAttribute("page", page);

        return "admin/comment/list";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteComment(@PathVariable(value = "id") String id) {
        commentService.deleteComment(commentService.getComment(id));
        return ControllerUtils.AJAX_OK;
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public Object create() {
        return "admin/comment/form";
    }

    /**
     * 文字评论
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Object create(@RequestParam(value = "screenName") String screenName,
                         @RequestParam(value = "text") String text,
                         @RequestParam(value = "postId", required = false) String postId,
                         @RequestParam(value = "replyCommentId", required = false) String replyCommentId,
                         Model model) {

        String formView = "admin/comment/form";

        if (StringUtils.isBlank(postId) && StringUtils.isBlank(replyCommentId)) {
            model.addAttribute(ControllerUtils.MODEL_ERROR_KEY, "postId或replyCommentId是必需的");
            return formView;
        }

        User user = accountService.findUserByScreenName(screenName);
        if (user == null) {
            model.addAttribute(ControllerUtils.MODEL_ERROR_KEY, "用户 " + screenName + " 不存在");
            return formView;
        }

        Comment comment = new Comment();

        //回复Comment
        if (StringUtils.isNotBlank(replyCommentId)) {

            Comment replyComment = commentService.getComment(replyCommentId);
            if (replyComment == null) {
                model.addAttribute(ControllerUtils.MODEL_ERROR_KEY,
                        "评论 " + replyCommentId + " 不存在");
                return formView;
            }

            comment.setReplyCommentId(replyCommentId);
            comment.setReplyCommentUserId(replyComment.getUserId());
            comment.setPostId(replyComment.getPostId());
            postId = comment.getPostId();
        }
        //回复Post
        else {
            Post post = statusService.getPost(postId);

            if (post == null) {
                model.addAttribute(ControllerUtils.MODEL_ERROR_KEY,
                        "帖子 " + postId + " 不存在");
                return formView;
            }

            if (!post.isPublic()) {
                model.addAttribute(ControllerUtils.MODEL_ERROR_KEY,
                        "不能评论非所有人可见的视频");
                return formView;
            }

            if (post.getOriginPostId() == null) {
                comment.setPostId(postId);
            } else {
                comment.setPostId(post.getOriginPostId());
            }
        }

        comment.setUserId(user.getId());
        comment.setText(text);

        commentService.create(comment, null, null, null);

        return "redirect:/admin/comment/list?postId=" + postId;
    }

}
