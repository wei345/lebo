package com.lebo.web;

import com.lebo.entity.Comment;
import com.lebo.entity.User;
import com.lebo.service.CommentService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.CommentListParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(CommentListParam commentListParam, Model model) {

        if (StringUtils.isNotBlank(commentListParam.getPostId())) {
            List<Comment> comments = commentService.list(commentListParam);

            List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>(comments.size());
            for (Comment comment : comments) {
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
                User user = accountService.getUser(comment.getUserId());
                map.put("screenName", user.getScreenName());
                map.put("createdAt", ControllerSetup.DEFAULT_DATE_FORMAT.format(comment.getCreatedAt()));
                maps.add(map);
            }

            model.addAttribute("count", commentListParam.getCount());
            model.addAttribute("postId", commentListParam.getPostId());
            model.addAttribute("comments", maps);
        }

        return "comment/list";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteComment(@PathVariable(value = "id") String id) {
        commentService.deleteComment(commentService.getComment(id));
        return ControllerUtils.AJAX_OK;
    }
}
