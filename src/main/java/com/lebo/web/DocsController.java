package com.lebo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Wei Liu
 * Date: 13-7-11
 * Time: PM4:46
 */
@Controller
@RequestMapping("/docs")
public class DocsController {

    @RequestMapping(value = "api/v1", method = RequestMethod.GET)
    public String index(){
        return "docs/api/v1/index";
    }

    @RequestMapping(value = "**", method = RequestMethod.GET)
    public String defaultView(HttpServletRequest request){
        return request.getRequestURI().substring(1);
    }
}
