package com.lebo.web.admin;

import com.lebo.entity.Channel;
import com.lebo.entity.FileInfo;
import com.lebo.service.AbstractMongoService;
import com.lebo.service.FileStorageService;
import com.lebo.service.SettingService;
import com.lebo.web.ControllerUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
    private FileStorageService fileStorageService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("channels", settingService.getAllChannels());
        return "admin/setting/channelList";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm() {
        return "admin/setting/channelForm";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("channel", settingService.getChannel(id));
        return "admin/setting/channelForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("channel") Channel channel,
                         @RequestParam(value = "channelImage", required = false) MultipartFile image,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            if (image != null) {
                //保存新文件
                FileInfo fileInfo = ControllerUtils.getFileInfo(image);
                fileInfo.setKey(AbstractMongoService.generateFileId("images/channels", null, channel.getSlug(), fileInfo.getLength(), fileInfo.getContentType(), fileInfo.getFilename()));
                fileStorageService.save(fileInfo);

                //删除旧文件
                if (channel.getImage() != null && !channel.getImage().equals(fileInfo.getKey())) {
                    fileStorageService.delete(channel.getImage());
                }

                channel.setImage(fileInfo.getKey());
            }
            settingService.saveChannel(channel);
            redirectAttributes.addFlashAttribute("success", "已保存 " + channel.getName());
            return "redirect:/admin/channels";
        } catch (Exception e) {
            model.addAttribute("error", e);
            model.addAttribute("channel", channel);
            return "admin/setting/channelForm";
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable(value = "id") String id,
                         RedirectAttributes redirectAttributes) {
        try {
            //删除图片
            Channel channel = settingService.getChannel(id);
            if (StringUtils.isNotBlank(channel.getImage())) {
                fileStorageService.delete(channel.getImage());
            }
            //删除频道
            settingService.deleteChannel(id);
            redirectAttributes.addFlashAttribute("success", "已删除 id=" + id);
            return "redirect:/admin/channels";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败 id=" + id);
            return "redirect:/admin/channels";
        }
    }

    @RequestMapping(value = "enableChannel/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String enableChannel(@PathVariable(value = "id") String id) {
        settingService.updateChannelEnabled(id, true);
        return "ok";
    }

    @RequestMapping(value = "disableChannel/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String disableChannel(@PathVariable(value = "id") String id) {
        settingService.updateChannelEnabled(id, false);
        return "ok";
    }

    @RequestMapping(value = "updateOrder", method = RequestMethod.POST)
    @ResponseBody
    public String updateChannelOrder(@RequestParam(value = "id") String id,
                                     @RequestParam(value = "order") int order) {
        settingService.updateChannelOrder(id, order);
        return "ok";
    }

    @ModelAttribute
    public void getUser(@RequestParam(value = "id", required = false) String id, Model model) {
        if (id != null) {
            model.addAttribute("channel", settingService.getChannel(id));
        }
    }
}
