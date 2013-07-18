package com.lebo.rest;

import com.lebo.entity.User;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
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
@RequestMapping(value = "/api/1/users")
public class UserRestController {
    @Autowired
    private AccountService accountService;

    //TODO 搜索用户，除了名字还查找位置和公司？
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public Object search(@Valid SearchParam param) {
        Page<User> page = accountService.searchUser(param);
        return accountService.toUserDtos(page.getContent());
    }

    //TODO 查看用户
    @RequestMapping(value = "show", method = RequestMethod.GET)
    @ResponseBody
    public Object show(@RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "screenName", required = false) String screenName) {
        String id = accountService.getUserId(userId,screenName);
        User user = accountService.getUser(id);
        return accountService.toUserDto(user);
    }

}
