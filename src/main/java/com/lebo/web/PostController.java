package com.lebo.web;

import com.lebo.entity.Post;
import com.lebo.entity.User;
import com.lebo.service.ServiceException;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PaginationParam;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "screenName", required = false) String screenName,
                       @Valid PaginationParam paginationParam,
                       Model model) {
        try {
            userId = accountService.getUserId(userId, screenName);
        } catch (ServiceException e) {
            userId = null;
        }

        List<Post> posts = statusService.findOriginPosts(userId, paginationParam);

        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>(posts.size());
        for (Post post : posts) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", post.getId());
            map.put("videoUrl", post.getVideo().getContentUrl());
            map.put("videoFirstFrameUrl", post.getVideoFirstFrameUrl());
            map.put("text", post.getText());
            User user = accountService.getUser(post.getUserId());
            map.put("screenName", user.getScreenName());
            map.put("createdAt", ControllerSetup.DEFAULT_DATE_FORMAT.format(post.getCreatedAt()));
            maps.add(map);
        }

        model.addAttribute("posts", maps);
        model.addAttribute("count", paginationParam.getCount());

        return "post/list";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object deletePublishVideoTask(@PathVariable(value = "id") String id) {
        statusService.destroyPost(id);
        return ControllerUtils.AJAX_OK;
    }
}
