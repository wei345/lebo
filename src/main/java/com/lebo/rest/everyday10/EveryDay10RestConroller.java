package com.lebo.rest.everyday10;

import com.lebo.entity.Post;
import com.lebo.service.StatusService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.TimelineParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * 小应用"每日十个"
 *
 * @author: Wei Liu
 * Date: 13-9-25
 * Time: PM5:58
 */
@Controller
@RequestMapping("/api/1/everyday10")
public class EveryDay10RestConroller {
    @Autowired
    private StatusService statusService;
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@Valid TimelineParam param,
                       @RequestParam(value = "count", defaultValue = "10") int count,
                       @RequestParam(value = "screenName", defaultValue = "每天笑十次") String screenName) {
        param.setCount(count);
        param.setScreenName(screenName);
        param.setUserId(accountService.getUserId(param.getUserId(), screenName));

        List<Post> posts = statusService.userTimeline(param);
        return statusService.toBasicStatusDtos(posts);
    }
}
