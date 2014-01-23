package com.lebo.web.admin;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.lebo.entity.Comment;
import com.lebo.entity.Post;
import com.lebo.entity.ReportSpam;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.CommentService;
import com.lebo.service.ReportSpamService;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PageRequest;
import com.lebo.web.ControllerSetup;
import com.lebo.web.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.BeanMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 14-1-16
 * Time: PM5:00
 */
@Controller
@RequestMapping("/admin/report-spam")
public class ReportSpamController {

    @Autowired
    private ReportSpamService reportSpamService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String list(PageRequest pageRequest,
                       @RequestParam(value = "processed", required = false) Boolean processed,
                       @RequestParam(value = "reportObjectType", required = false) ReportSpam.ObjectType reportObjectType,
                       @RequestParam(value = "reportUserId", required = false) String reportUserId,
                       @RequestParam(value = "informerUserId", required = false) String informerUserId,
                       Model model) {

        long beginTime = System.currentTimeMillis();

        Page<ReportSpam> page = reportSpamService.list(
                processed,
                reportObjectType,
                reportUserId,
                informerUserId,
                pageRequest);

        List<Map<String, Object>> detailList = Lists.transform(
                page.getContent(),
                new Function<ReportSpam, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> apply(ReportSpam reportSpam) {
                        return getReportSpamDetail(reportSpam);
                    }
                });

        model.addAttribute("page", page);
        model.addAttribute("list", detailList);
        model.addAttribute("spentSeconds", (System.currentTimeMillis() - beginTime) / 1000);

        return "admin/reportSpamList";
    }

    @RequestMapping(value = "setProcessed", method = RequestMethod.POST)
    @ResponseBody
    public Object setProcessed(@RequestParam("id") String id,
                               @RequestParam("processed") boolean processed) {

        reportSpamService.setProcessed(id, processed, accountService.getCurrentUserId());

        return ControllerUtils.AJAX_OK;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Object delete(@RequestParam("id") String id) {

        reportSpamService.delete(id);

        return ControllerUtils.AJAX_OK;
    }

    @RequestMapping(value = "deleteReportObject", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteReportObject(@RequestParam("id") String id) {

        ReportSpam reportSpam = reportSpamService.get(id);

        if (reportSpam != null) {
            switch (reportSpam.getReportObjectType()) {
                case POST:
                    statusService.destroyPost(reportSpam.getReportObjectId());
                    break;
                case COMMENT:
                    commentService.deleteComment(commentService.getComment(reportSpam.getReportObjectId()));
                    break;
                default:
                    return ErrorDto.badRequest("未知类型 " + reportSpam.getReportObjectType());
            }

            reportSpamService.setProcessed(id, true, accountService.getCurrentUserId());
        }

        return ControllerUtils.AJAX_OK;
    }

    private Map<String, Object> getReportSpamDetail(ReportSpam reportSpam) {

        @SuppressWarnings("unchecked")
        Map<String, Object> detail = BeanMapper.map(reportSpam, Map.class);

        detail.put("reportUser", accountService.getUser(reportSpam.getReportUserId()));

        detail.put("informerUser", accountService.getUser(reportSpam.getInformerUserId()));

        if (reportSpam.getProcessUserId() != null) {
            detail.put("processUser", accountService.getUser(reportSpam.getProcessUserId()));
        }

        detail.put("reportObjectType", reportSpam.getReportObjectType());

        switch (reportSpam.getReportObjectType()) {
            case POST:
                detail.put("reportObject", getPostModel(reportSpam.getReportObjectId()));
                break;
            case COMMENT:
                detail.put("reportObject", getCommentModel(reportSpam.getReportObjectId()));
                break;
        }

        return detail;
    }

    private Map<String, Object> getPostModel(String postId) {

        Post post = statusService.getPost(postId);

        if (post == null) {
            return null;
        }

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

        return postInfo;
    }

    private Map<String, Object> getCommentModel(String commentId) {
        Comment comment = commentService.getComment(commentId);

        if (comment == null) {
            return null;
        }

        Map<String, Object> info = new HashMap<String, Object>();

        info.put("id", comment.getId());

        //视频评论
        if (comment.getVideo() != null) {
            info.put("videoUrl", comment.getVideo().getContentUrl());
            info.put("duration", comment.getVideo().getDuration());
            info.put("videoFirstFrameUrl", comment.getVideoFirstFrameUrl());
        }
        //语音评论
        if (comment.getAudio() != null) {
            info.put("audioUrl", comment.getAudio().getContentUrl());
            info.put("duration", comment.getAudio().getDuration());
        }
        info.put("text", comment.getText());
        info.put("createdAt", ControllerSetup.DEFAULT_DATE_FORMAT.format(comment.getCreatedAt()));

        return info;
    }


}
