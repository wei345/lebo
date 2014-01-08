package com.lebo.rest.everyday10;

import com.lebo.entity.Post;
import com.lebo.service.SettingService;
import com.lebo.service.StatusService;
import com.lebo.service.param.TimelineParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Collections;
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
    private SettingService settingService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(@Valid TimelineParam param,
                       @RequestParam(value = "count", defaultValue = "10") int count) {

        String userId = settingService.getSetting().getEveryday10AccountId();

        if (StringUtils.isBlank(userId)) {
            return Collections.EMPTY_LIST;
        }

        param.setCount(count);
        param.setUserId(userId);

        List<Post> posts = statusService.userTimeline(param);
        return statusService.toBasicStatusDtos(posts);
    }
}
