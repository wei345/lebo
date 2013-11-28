package com.lebo.web.admin;

import com.lebo.entity.Statistics;
import com.lebo.service.StatisticsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-11-27
 * Time: PM7:02
 */
@RequestMapping("/admin/statistics")
@Controller
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping(value = "charts", method = RequestMethod.GET)
    public String charts(@RequestParam(value = "startDate", required = false) String startDateStr,
                         @RequestParam(value = "endDate", required = false) String endDateStr,
                         Model model) throws ParseException {

        Date endDate = StringUtils.isNotBlank(endDateStr) ? sdf.parse(endDateStr) : new Date();
        Date startDate = StringUtils.isNotBlank(startDateStr) ? sdf.parse(startDateStr) : prevMonth(endDate);

        List<Statistics> dailyList = statisticsService.getDaily(startDate, endDate);

        model.addAttribute("dailyList", dailyList);
        model.addAttribute("startDate", sdf.format(startDate));
        model.addAttribute("endDate", sdf.format(endDate));

        return "admin/statistics/charts";
    }

    private Date prevMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }
}
