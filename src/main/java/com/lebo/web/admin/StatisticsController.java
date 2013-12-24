package com.lebo.web.admin;

import com.lebo.entity.ActiveUser;
import com.lebo.entity.Statistics;
import com.lebo.service.StatisticsService;
import com.lebo.web.ControllerSetup;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.mapper.BeanMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    private static SimpleDateFormat sdf = ControllerSetup.ADMIN_QUERY_DATE_FORMAT;

    private static Date startDate; //统计起始日期

    static {
        try {
            startDate = sdf.parse("2013-12-21");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "im", method = RequestMethod.GET)
    public String im(@RequestParam(value = "startDate", required = false) String startDateStr,
                     @RequestParam(value = "endDate", required = false) String endDateStr,
                     Model model) throws ParseException {

        Date endDate = StringUtils.isNotBlank(endDateStr) ? DateUtils.addDays(sdf.parse(endDateStr), 1) : new Date();
        Date startDate = StringUtils.isNotBlank(startDateStr) ? sdf.parse(startDateStr) : DateUtils.addDays(endDate, -7);

        List<Statistics> dailyList = statisticsService.getDaily(startDate, endDate);

        model.addAttribute("dailyList", dailyList);
        model.addAttribute("startDate", sdf.format(startDate));
        model.addAttribute("endDate", sdf.format(DateUtils.addDays(endDate, -1)));
        model.addAttribute("today", sdf.format(new Date()));

        return "admin/statistics/im";
    }

    @RequestMapping(value = "activeUser", method = RequestMethod.GET)
    public String activeUser(@RequestParam(value = "start", required = false) String start,
                             @RequestParam(value = "end", required = false) String end,
                             Model model) throws ParseException {

        if (StringUtils.isBlank(end)) {
            end = sdf.format(new Date());
        }

        if (StringUtils.isBlank(start)) {
            start = sdf.format(DateUtils.addDays(sdf.parse(end), -7));
        }

        List<ActiveUser> list = statisticsService.getActiveUser(start, end);

        List<Map> mapList = BeanMapper.mapList(list, Map.class);
        for (Map map : mapList) {
            map.put("statisticsDays", getStatisticsDays((String) map.get("id")));
        }

        model.addAttribute("list", mapList);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("today", sdf.format(new Date()));

        return "admin/statistics/activeUser";
    }

    private static int getStatisticsDays(Date date) {

        long time = date.getTime() - startDate.getTime();

        int days = (int) (time / DateUtils.MILLIS_PER_DAY);

        return (time % DateUtils.MILLIS_PER_DAY == 0) ? days : days + 1;
    }

    private static int getStatisticsDays(String yyyyMMdd) {
        try {
            return getStatisticsDays(sdf.parse(yyyyMMdd)) + 1;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
