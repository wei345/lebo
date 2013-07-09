package com.lebo.rest;

import com.lebo.rest.dto.ErrorDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Wei Liu
 * Date: 13-7-1
 * Time: AM11:23
 */
@Controller
@RequestMapping(value = "/api")
public class HomeRestController {
    @RequestMapping(value = "")
    @ResponseBody
    public Object index() {
        return "It works";
    }

    @RequestMapping(value = "**")
    @ResponseBody
    public Object notFound() {
        return ErrorDto.NOT_FOUND;
    }
}
