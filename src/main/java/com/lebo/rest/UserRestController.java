package com.lebo.rest;

import com.lebo.service.account.AccountService;
import com.lebo.service.param.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public Object search(SearchParam param) {
        return accountService.searchUser(param);
    }
}
