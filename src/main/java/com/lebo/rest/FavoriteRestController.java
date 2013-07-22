package com.lebo.rest;

import com.lebo.entity.Post;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.FavoriteService;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PaginationParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * @author: Wei Liu
 * Date: 13-7-12
 * Time: PM12:16
 */
@Controller
@RequestMapping("/api/1/favorites")
public class FavoriteRestController {
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StatusService statusService;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@RequestParam("id") String id) {
        String userId = accountService.getCurrentUserId();

        if (favoriteService.isFavorited(userId, id)) {
            return ErrorDto.newBadRequestError("你已经收藏了");
        }

        favoriteService.create(userId, id);
        Post post = statusService.findPost(id);
        return statusService.toStatusDto(post);
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Object show(@Valid PaginationParam param) {
        return favoriteService.list(accountService.getCurrentUserId(), param);
    }

    @RequestMapping(value = "destroy", method = RequestMethod.POST)
    @ResponseBody
    public Object destroy(@RequestParam("postId") String postId) {
        String userId = accountService.getCurrentUserId();
        Post post = statusService.findPost(postId);
        if (post == null) {
            return ErrorDto.newBadRequestError("The parameter id [" + postId + "] is invalid.");
        }

        String originId = (post.getOriginPostId() == null ? postId : post.getOriginPostId());

        if (favoriteService.isFavorited(userId, originId)) {
            favoriteService.destroy(userId, originId);
        }else{
            favoriteService.create(userId, originId);
        }

        return statusService.toStatusDto(post);
    }
}
