package com.lebo.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: Wei Liu
 * Date: 13-11-27
 * Time: PM7:02
 */
@RequestMapping("/admin/statistics")
@Controller
public class StatisticsController {

    @RequestMapping(value = "charts", method = RequestMethod.GET)
    public String charts(@RequestParam(value = "startDate", required = false) String startDate,
                             @RequestParam(value = "endDate", required = false) String endDate) {



        return "admin/statistics/charts";
    }
}
