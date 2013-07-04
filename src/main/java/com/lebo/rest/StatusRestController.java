package com.lebo.rest;

import com.lebo.service.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: Wei Liu
 * Date: 13-7-3
 * Time: PM8:35
 */
@Controller
@RequestMapping(value = "/api/v1/statuses")
public class StatusRestController {
    @Autowired
    private StatusService statusService;


}
