package com.lebo.web;

import com.lebo.entity.Post;
import com.lebo.rest.dto.StatusDto;
import com.lebo.service.FileContentUrlUtils;
import com.lebo.service.SettingService;
import com.lebo.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author: Wei Liu
 * Date: 13-8-17
 * Time: PM4:15
 */
@RequestMapping("/play")
@Controller
public class PlayController {

    @Autowired
    private StatusService statusService;
    @Autowired
    private Properties applicationProperties;
    @Autowired
    private SettingService settingService;

    private Pattern mobilePattern = Pattern.compile("Android|webOS|iPhone|iPad|iPod|BlackBerry");
    private Pattern desktopPattern = Pattern.compile("Mozilla|MSIE|Windows NT|Mac OS X");

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String play(@PathVariable("id") String id, @RequestHeader("User-Agent") String userAgent, Model model) {
        Post post = statusService.getPost(id);

        if (post == null) {
            return "play/not-found";
        }

        StatusDto dto = statusService.toBasicStatusDto(post);
        model.addAttribute("post", dto.getOriginStatus() == null ? dto : dto.getOriginStatus());
        model.addAttribute("baseurl", applicationProperties.get("app.baseurl"));
        model.addAttribute("image_dl_iphone_app_url", FileContentUrlUtils.getContentUrl("images/btn-dl-lebo-iphone.png"));
        model.addAttribute("image_dl_android_app_url", FileContentUrlUtils.getContentUrl("images/btn-dl-lebo-android.png"));
        model.addAttribute("appStoreLeboUrl", settingService.getSetting().getAppStoreLeboUrl());
        model.addAttribute("leboAppAndroidDownloadUrl", settingService.getSetting().getLeboAppAndroidDownloadUrl());
        model.addAttribute("pvt", post.getPvt());

        //移动版页面
        if (mobilePattern.matcher(userAgent).find()) {
            return "play/video-mobile";
        }
        //桌面版页面
        if (desktopPattern.matcher(userAgent).find()) {
            return "play/video-desktop";
        }
        //默认手机版
        return "play/video-mobile";
    }
}
