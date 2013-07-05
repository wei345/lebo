package com.lebo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: Wei Liu
 * Date: 13-7-5
 * Time: PM2:25
 */
@Controller
public class HomeController {
    @RequestMapping(value = "restclient", method = RequestMethod.GET)
    public String restClient(){
        return "restclient";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(){
        return "login";
    }
}
