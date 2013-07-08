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
@RequestMapping
public class HomeController {
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "restclient", method = RequestMethod.GET)
    public String restClient() {
        return "restclient";
    }
}
