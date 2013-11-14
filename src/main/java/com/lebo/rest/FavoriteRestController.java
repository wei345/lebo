package com.lebo.rest;

import com.lebo.entity.Favorite;
import com.lebo.entity.Post;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.FavoriteService;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PaginationParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    private Sort FAVORITE_LIST_SORT = new Sort(Sort.Direction.DESC, Favorite.POST_ID_KEY);

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@RequestParam("id") String id) {
        String userId = accountService.getCurrentUserId();

        if (favoriteService.isFavorited(userId, id)) {
            return ErrorDto.badRequest("你已经收藏了");
        }

        favoriteService.create(userId, id);
        Post post = statusService.getPost(id);
        return statusService.toStatusDto(post);
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "screenName", required = false) String screenName,
                       @Valid PaginationParam paginationParam) {

        paginationParam.setSort(FAVORITE_LIST_SORT);

        if (StringUtils.isBlank(userId) && StringUtils.isBlank(screenName)) {
            userId = accountService.getCurrentUserId();
        } else {
            userId = accountService.getUserId(userId, screenName);
        }

        return statusService.toStatusDtos(favoriteService.list(userId, paginationParam));
    }

    @RequestMapping(value = "destroy", method = RequestMethod.POST)
    @ResponseBody
    public Object destroy(@RequestParam("id") String id) {
        Post post = statusService.getPost(id);
        if (post == null) {
            return ErrorDto.badRequest("参数id [" + id + "] 无效");
        }

        favoriteService.destroy(accountService.getCurrentUserId(), statusService.getOriginPostId(post));
        return statusService.toStatusDto(post);
    }
}
