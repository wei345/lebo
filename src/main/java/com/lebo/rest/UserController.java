package com.lebo.rest;

import com.lebo.service.SearchParam;
import com.lebo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Wei Liu
 * Date: 13-7-9
 * Time: AM10:32
 */
@RequestMapping(value = "/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public Object search(SearchParam param){
        return userService.searchUser(param);
    }
}
