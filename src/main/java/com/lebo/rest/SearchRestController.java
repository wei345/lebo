package com.lebo.rest;

import com.lebo.service.StatusService;
import com.lebo.service.param.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * @author: Wei Liu
 * Date: 13-7-12
 * Time: PM10:20
 */
@Controller
@RequestMapping("/api/v1/search")
public class SearchRestController {

    @Autowired
    private StatusService statusService;

    @RequestMapping(value = "posts", method = RequestMethod.GET)
    @ResponseBody
    public Object posts(@Valid SearchParam param) {
        return statusService.toStatusDtoList(statusService.searchPosts(param));
    }
}
