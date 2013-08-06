package com.lebo.web.setting;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.lebo.entity.Setting;
import com.lebo.service.GridFsService;
import com.lebo.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Collections3;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-18
 * Time: PM4:59
 */
@Controller
@RequestMapping("/admin/channels")
public class ChannelController {
    @Autowired
    private SettingService settingService;

    @Autowired
    private GridFsService gridFsService;

    private JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        Setting setting = settingService.getSetting();
        model.addAttribute("channels", setting.getChannels());

        jsonMapper.getMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
        String json = jsonMapper.toJson(setting.getChannels());

        model.addAttribute("channelsJson", json);
        return "setting/channelList";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String form() {
        return "setting/channelForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid Setting.Channel channel,
                         @RequestParam(value = "channelImage") MultipartFile image,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            String fileId = gridFsService.save(image.getInputStream(), image.getOriginalFilename(), image.getContentType());
            channel.setImage(fileId);

            Setting setting = settingService.getSetting();

            for (Setting.Channel c : setting.getChannels()) {
                if (channel.getName().equals(c.getName())) {
                    redirectAttributes.addFlashAttribute("error", "重复 " + channel.getName());
                    return "redirect:/admin/channels";
                }
            }

            setting.getChannels().add(channel);
            settingService.saveOption(setting);

            redirectAttributes.addFlashAttribute("success", "已创建 " + channel.getName());
            return "redirect:/admin/channels";
        } catch (Exception e) {
            model.addAttribute("error", e);
            return "setting/channelForm";
        }
    }

    @RequestMapping(value = "preview", method = RequestMethod.POST)
    public String preview(@RequestParam("channels") String json, Model model) {
        List<Setting.Channel> channels = jsonMapper.fromJson(json, jsonMapper.createCollectionType(ArrayList.class, Setting.Channel.class));
        model.addAttribute("channels", channels);

        return "setting/channelPreview";
    }

    //TODO 检查频道修改，删除图片
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@RequestParam("channels") String json, RedirectAttributes redirectAttributes) {
        List<Setting.Channel> channels = jsonMapper.fromJson(json, jsonMapper.createCollectionType(ArrayList.class, Setting.Channel.class));
        Setting setting = settingService.getSetting();

        //删除已不用的图片
        List<String> oldImages = Collections3.extractToList(setting.getChannels(), "image");
        List<String> newImages = Collections3.extractToList(channels, "image");
        for (String fileId : oldImages) {
            if (!newImages.contains(fileId)) {
                gridFsService.delete(fileId);
            }
        }

        redirectAttributes.addFlashAttribute("success", "保存成功");
        setting.setChannels(channels);
        settingService.saveOption(setting);
        return "redirect:/admin/channels";
    }
}
