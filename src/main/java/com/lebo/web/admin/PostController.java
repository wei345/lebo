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
        try {
            userId = accountService.getUserId(userId, screenName);
        } catch (ServiceException e) {
            userId = null;
        }

        pageRequest.setSort(new Sort(Sort.Direction.fromString(order), orderBy));

        Page<Post> page = statusService.findOriginPosts(userId, track, pageRequest);

        List<Post> posts = page.getContent();

        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>(posts.size());
        for (Post post : posts) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", post.getId());
            map.put("videoUrl", post.getVideo().getContentUrl());
            map.put("videoFirstFrameUrl", post.getVideoFirstFrameUrl());
            map.put("text", post.getText());
            map.put("favoritesCount", post.getFavoritesCount());
            map.put("viewCount", post.getViewCount());
            map.put("rating", post.getRating());
            map.put("createdAt", ControllerSetup.DEFAULT_DATE_FORMAT.format(post.getCreatedAt()));
            map.put("repostCount", statusService.countReposts(post.getId()));
            map.put("commentCount", commentService.countPostComments(post.getId()));
            User user = accountService.getUser(post.getUserId());
            map.put("userId", user.getId());
            map.put("screenName", user.getScreenName());
            map.put("profileImageUrl", user.getProfileImageUrl());
            maps.add(map);
        }

        model.addAttribute("posts", maps);
        model.addAttribute("count", pageRequest.getPageSize());
        model.addAttribute("page", page);

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
        statusService.refreshHotPosts();
        return ControllerUtils.AJAX_OK;
    }

    @RequestMapping(value = "refreshHotPosts", method = RequestMethod.GET)
    public Object refreshHotPostsShow() {
        return "/admin/post/refreshHotPosts";
    }
}
