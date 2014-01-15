package com.lebo.rest;

import com.lebo.entity.ReportSpam;
import com.lebo.service.ReportSpamService;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Wei Liu
 * Date: 14-1-15
 * Time: PM1:58
 */
@Controller
public class ReportSpamRestController {

    @Autowired
    private ReportSpamService reportSpamService;
    @Autowired
    private AccountService accountService;
    public static final String PREFIX_API_1_1 = "/api/1.1/";

    @RequestMapping(value = PREFIX_API_1_1 + "reportSpam.json", method = RequestMethod.POST)
    @ResponseBody
    public Object reportSpam(@RequestParam("reportUserId") String reportUserId,
                             @RequestParam(value = "reportType", required = false) ReportSpam.ReportType reportType,
                             @RequestParam("reportObjectType") ReportSpam.ObjectType reportObjectType,
                             @RequestParam("reportObjectId") String reportObjectId,
                             @RequestParam(value = "reportNotes", required = false) String reportNotes) {

        ReportSpam reportSpam = new ReportSpam(
                reportUserId,
                reportType,
                reportObjectType,
                reportObjectId,
                reportNotes,
                accountService.getCurrentUserId());

        reportSpamService.create(reportSpam);

        return reportSpam;
    }

}
