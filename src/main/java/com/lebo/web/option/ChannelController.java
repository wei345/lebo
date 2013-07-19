package com.lebo.web.option;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.lebo.entity.Option;
import com.lebo.service.GridFsService;
import com.lebo.service.OptionService;
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
    private OptionService optionService;

    @Autowired
    private GridFsService gridFsService;

    private JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        Option option = optionService.getOption();
        model.addAttribute("channels", option.getChannels());

        jsonMapper.getMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
        String json = jsonMapper.toJson(option.getChannels());

        model.addAttribute("channelsJson", json);
        return "option/channelList";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String form() {
        return "option/channelForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "name") String name,
                         @RequestParam(value = "content") String content,
                         @RequestParam(value = "image") MultipartFile image,
                         @RequestParam(value = "backgroundColor") String backgroundColor,
                         @RequestParam(value = "enabled", defaultValue = "false") boolean enabled,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            String fileId = gridFsService.save(image.getInputStream(), image.getOriginalFilename(), image.getContentType());

            Option.Channel channel = new Option.Channel(name, content, fileId, backgroundColor, enabled);
            Option option = optionService.getOption();
            option.getChannels().add(channel);
            optionService.saveOption(option);

            redirectAttributes.addFlashAttribute("success", "已创建 " + name);
            return "redirect:/admin/channels";
        } catch (Exception e) {
            model.addAttribute("error", e);
            return "option/channelForm";
        }
    }

    @RequestMapping(value = "preview", method = RequestMethod.POST)
    public String preview(@RequestParam("channels") String json, Model model) {
        List<Option.Channel> channels = jsonMapper.fromJson(json, jsonMapper.createCollectionType(ArrayList.class, Option.Channel.class));
        model.addAttribute("channels", channels);

        return "option/channelPreview";
    }

    //TODO 检查频道修改，删除图片
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@RequestParam("channels") String json, RedirectAttributes redirectAttributes) {
        List<Option.Channel> channels = jsonMapper.fromJson(json, jsonMapper.createCollectionType(ArrayList.class, Option.Channel.class));
        Option option = optionService.getOption();

        //删除已不用的图片
        List<String> oldImages = Collections3.extractToList(option.getChannels(), "image");
        List<String> newImages = Collections3.extractToList(channels, "image");
        for(String fileId : oldImages){
            if(!newImages.contains(fileId)){
                gridFsService.delete(fileId);
            }
        }

        redirectAttributes.addFlashAttribute("success", "保存成功");
        option.setChannels(channels);
        optionService.saveOption(option);
        return "redirect:/admin/channels";
    }
}
