package com.lebo.web.admin;

import com.lebo.entity.Post;
import com.lebo.entity.Setting;
import com.lebo.entity.User;
import com.lebo.service.CommentService;
import com.lebo.service.SettingService;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PageRequest;
import com.lebo.web.ControllerSetup;
import com.lebo.web.ControllerUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private SettingService settingService;

    private SimpleDateFormat sdf = ControllerSetup.ADMIN_QUERY_DATE_FORMAT;

    private static int HOT_POST_COUNT = 60;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(@RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "screenName", required = false) String screenName,
                       @RequestParam(value = "track", required = false) String track,
                       @RequestParam(value = "postId", required = false) String postId,
                       @RequestParam(value = "orderBy", defaultValue = Post.ID_KEY) String orderBy,
                       @RequestParam(value = "order", defaultValue = "DESC") String order,
                       @RequestParam(value = "startDate", required = false) String startDateStr,
                       @RequestParam(value = "endDate", required = false) String endDateStr,
                       @Valid PageRequest pageRequest,
                       Model model) throws ParseException {

        long beginTime = System.currentTimeMillis();

        Date endDate = StringUtils.isNotBlank(endDateStr) ? DateUtils.addDays(sdf.parse(endDateStr), 1) : null;
        Date startDate = StringUtils.isNotBlank(startDateStr) ? sdf.parse(startDateStr) : null;

        Page<Post> page = null;

        //读取screenName的userId
        if (StringUtils.isNotBlank(screenName)) {
            User user = accountService.findUserByScreenName(screenName);
            //使用screenName的userId
            if (StringUtils.isBlank(userId)) {
                userId = user.getId();
            }
            //检查参数中的screenName和userId是否对应
            else {
                if (!user.getId().equals(userId)) {
                    page = new PageImpl<Post>(Collections.EMPTY_LIST);
                }
            }
        }

        //根据条件查询
        if (page == null) {
            pageRequest.setSort(new Sort(Sort.Direction.fromString(order), orderBy));
            page = statusService.findOriginPosts(userId, track, postId, pageRequest, startDate, endDate);
        }

        model.addAttribute("posts", toModelPosts(page.getContent()));
        model.addAttribute("page", page);
        model.addAttribute("spentSeconds", (System.currentTimeMillis() - beginTime) / 1000.0);
        model.addAttribute("controllerMethod", "list");
        model.addAttribute("startDate", startDate == null ? null : sdf.format(startDate));
        model.addAttribute("endDate", endDate == null ? null : sdf.format(DateUtils.addDays(endDate, -1)));
        model.addAttribute("today", sdf.format(new Date()));

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
    public Object refreshHotPosts(RedirectAttributes redirectAttributes) {
        long beginTime = System.currentTimeMillis();

        statusService.refreshHotPosts();

        redirectAttributes.addFlashAttribute("success", "已更新热门帖子，用时 " + (System.currentTimeMillis() - beginTime) + " ms");

        return "redirect:/admin/post/hot";
    }

    @RequestMapping(value = "hot", method = RequestMethod.GET)
    public Object hotPosts(Model model) {
        long beginTime = System.currentTimeMillis();

        Setting setting = settingService.getSetting();

        Page<Post> page = new PageImpl<Post>(statusService.hotPosts(0, HOT_POST_COUNT));

        model.addAttribute("posts", toModelPosts(page.getContent()));
        model.addAttribute("page", page);
        model.addAttribute("hotDays", setting.getHotDays());
        model.addAttribute("maxHotPostCountPerUser", setting.getMaxHotPostCountPerUser());
        model.addAttribute("spentSeconds", (System.currentTimeMillis() - beginTime) / 1000.0);
        model.addAttribute("controllerMethod", "hotPosts");

        return "admin/post/list";
    }

    private List<Map<String, Object>> toModelPosts(List<Post> posts) {
        List<Map<String, Object>> postInfos = new ArrayList<Map<String, Object>>(posts.size());
        for (Post post : posts) {
            Map<String, Object> postInfo = new HashMap<String, Object>();

            postInfo.put("id", post.getId());
            postInfo.put("videoUrl", post.getVideo().getContentUrl());
            postInfo.put("duration", post.getVideo().getDuration());
            postInfo.put("videoFirstFrameUrl", post.getVideoFirstFrameUrl());
            postInfo.put("text", post.getText());
            postInfo.put("favoritesCount", post.getFavoritesCount());
            postInfo.put("viewCount", post.getViewCount());
            postInfo.put("rating", post.getRating());
            postInfo.put("pvt", post.getPvt());
            postInfo.put("popularity", post.getPopularity() == null ? 0 : post.getPopularity());
            postInfo.put("createdAt", ControllerSetup.DEFAULT_DATE_FORMAT.format(post.getCreatedAt()));
            postInfo.put("repostCount", statusService.countReposts(post.getId()));
            postInfo.put("commentCount", commentService.countPostComments(post.getId()));
            User user = accountService.getUser(post.getUserId());
            postInfo.put("userId", user.getId());
            postInfo.put("screenName", user.getScreenName());
            postInfo.put("profileImageUrl", user.getProfileImageUrl());

            postInfos.add(postInfo);
        }
        return postInfos;
    }
}
