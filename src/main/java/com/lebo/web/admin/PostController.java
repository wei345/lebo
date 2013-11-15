package com.lebo.web.admin;

import com.lebo.entity.Post;
import com.lebo.entity.User;
import com.lebo.service.CommentService;
import com.lebo.service.ServiceException;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PageRequest;
import com.lebo.web.ControllerSetup;
import com.lebo.web.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理帖子.
 *
 * @author: Wei Liu
 * Date: 13-9-28
 * Time: PM8:47
 */
@RequestMapping("/admin/post")
@Controller
public class PostController {
    @Autowired
    private StatusService statusService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "screenName", required = false) String screenName,
                       @RequestParam(value = "track", required = false) String track,
                       @RequestParam(value = "orderBy", defaultValue = Post.ID_KEY) String orderBy,
                       @RequestParam(value = "order", defaultValue = "DESC") String order,
                       @Valid PageRequest pageRequest,
                       Model model) {
        long beginTime = System.currentTimeMillis();

        try {
            userId = accountService.getUserId(userId, screenName);
        } catch (ServiceException e) {
            userId = null;
        }

        pageRequest.setSort(new Sort(Sort.Direction.fromString(order), orderBy));

        Page<Post> page = statusService.findOriginPosts(userId, track, pageRequest);

        List<Post> posts = page.getContent();

        List<Map<String, Object>> postInfos = new ArrayList<Map<String, Object>>(posts.size());
        for (Post post : posts) {
            Map<String, Object> postInfo = new HashMap<String, Object>();
            postInfo.put("id", post.getId());
            postInfo.put("videoUrl", post.getVideo().getContentUrl());
            postInfo.put("videoFirstFrameUrl", post.getVideoFirstFrameUrl());
            postInfo.put("text", post.getText());
            postInfo.put("favoritesCount", post.getFavoritesCount());
            postInfo.put("viewCount", post.getViewCount());
            postInfo.put("rating", post.getRating());
            postInfo.put("createdAt", ControllerSetup.DEFAULT_DATE_FORMAT.format(post.getCreatedAt()));
            postInfo.put("repostCount", statusService.countReposts(post.getId()));
            postInfo.put("commentCount", commentService.countPostComments(post.getId()));
            User user = accountService.getUser(post.getUserId());
            postInfo.put("userId", user.getId());
            postInfo.put("screenName", user.getScreenName());
            postInfo.put("profileImageUrl", user.getProfileImageUrl());
            postInfos.add(postInfo);
        }

        model.addAttribute("posts", postInfos);
        model.addAttribute("page", page);
        model.addAttribute("spentSeconds", (System.currentTimeMillis() - beginTime)/1000.0);

        return "admin/post/list";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object deletePublishVideoTask(@PathVariable(value = "id") String id) {
        statusService.destroyPost(id);
        return ControllerUtils.AJAX_OK;
    }

    @RequestMapping(value = "updateRating", method = RequestMethod.POST)
    @ResponseBody
    public Object updateRating(@RequestParam(value = "id") String id,
                               @RequestParam(value = "rating") int rating) {
        statusService.updateRating(id, rating);
        return ControllerUtils.AJAX_OK;
    }

    @RequestMapping(value = "refreshHotPosts", method = RequestMethod.POST)
    @ResponseBody
    public Object refreshHotPosts() {
        long beginTime = System.currentTimeMillis();

        statusService.refreshHotPosts();

        Map map = new HashMap(2);
        map.put("ok", true);
        map.put("time", System.currentTimeMillis() - beginTime);

        return map;
    }

    @RequestMapping(value = "refreshHotPosts", method = RequestMethod.GET)
    public Object refreshHotPostsShow() {
        return "/admin/post/refreshHotPosts";
    }
}
