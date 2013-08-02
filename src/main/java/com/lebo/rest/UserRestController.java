package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.service.SuggestionsService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PaginationParam;
import com.lebo.service.param.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Users.
 * 用户可以修改screenName，关注和粉丝不变。
 *
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: AM10:32
 */
@Controller
@RequestMapping("/api/1/users")
public class UserRestController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private SuggestionsService suggestionsService;

    //TODO 搜索用户，除了名字还查找位置和公司？
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public Object search(@RequestParam(value = "q", required = false) String q,
                         @RequestParam(value = "page", defaultValue = "0") int pageNo,
                         @RequestParam(value = "size", defaultValue = PaginationParam.DEFAULT_COUNT + "") int size,
                         @RequestParam(value = "orderBy", defaultValue = "_id") String orderBy,
                         @RequestParam(value = "order", defaultValue = "desc") String order) {
        if (orderBy.equals("id")) {
            orderBy = "_id";
        }

        Sort.Direction direction;
        if (order.equals("desc")) {
            direction = Sort.Direction.DESC;
        } else if (order.equals("asc")) {
            direction = Sort.Direction.ASC;
        } else {
            return ErrorDto.badRequest(String.format("参数order值[%s]无效", order));
        }

        if (User.FOLLOWERS_COUNT_KEY.equals(orderBy) || User.BE_FAVORITED_COUNT_KEY.equals(orderBy) ||
                User.VIEWS_COUNT_KEY.equals(orderBy) || "_id".equals(orderBy)) {
            //搜索
            SearchParam param = new SearchParam(q, pageNo, size, direction, orderBy);
            List<User> users = accountService.searchUser(param);
            return accountService.toUserDtos(users);

        } else {
            return ErrorDto.badRequest(String.format("参数orderBy值[%s]无效", orderBy));
        }
    }

    /**
     * 查看用户
     */
    @RequestMapping(value = "show", method = RequestMethod.GET)
    @ResponseBody
    public Object show(@RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "screenName", required = false) String screenName) {
        String id = accountService.getUserId(userId, screenName);
        User user = accountService.getUser(id);

        if (user == null) {
            return ErrorDto.notFound();
        }

        return accountService.toUserDto(user);
    }

    @RequestMapping(value = "suggestions/hot", method = RequestMethod.GET)
    @ResponseBody
    public Object suggestionsHot(@RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = PaginationParam.DEFAULT_COUNT + "") int size) {
        List<User> users = suggestionsService.getHotUsers(new PageRequest(page, size));
        return accountService.toUserDtos(users);
    }
}
