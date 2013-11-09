package com.lebo.web.admin;

import com.lebo.entity.Ad;
import com.lebo.entity.FileInfo;
import com.lebo.service.AbstractMongoService;
import com.lebo.service.AdService;
import com.lebo.service.FileStorageService;
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
 * 广告管理。
 *
 * @author: Wei Liu
 * Date: 13-11-9
 * Time: PM12:20
 */
@Controller
@RequestMapping("/admin/ads")
public class AdAdminController {

    @Autowired
    private AdService adService;
    @Autowired
    private FileStorageService fileStorageService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "group", required = false) String group,
                       @RequestParam(value = "enabled", required = false) Boolean enabled,
                       Model model) {
        model.addAttribute("ads", adService.findAd(group, enabled));
        return "admin/ad/list";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm() {
        return "admin/ad/form";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("ad", adService.getById(id));
        return "admin/ad/form";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("ad") Ad ad,
                         @RequestParam(value = "image", required = false) MultipartFile image,
                         @RequestParam(value = "imageSlug", required = false) String imageSlug,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            if (image != null) {
                //保存新文件
                FileInfo fileInfo = ControllerUtils.getFileInfo(image);
                fileInfo.setKey(AbstractMongoService.generateFileId("images/ads", null, imageSlug, fileInfo.getLength(), fileInfo.getContentType(), fileInfo.getFilename()));
                fileStorageService.save(fileInfo);

                //删除旧文件
                if (ad.getImageKey() != null && !ad.getImageKey().equals(fileInfo.getKey())) {
                    fileStorageService.delete(ad.getImageKey());
                }

                ad.setImageKey(fileInfo.getKey());
            }
            adService.save(ad);
            redirectAttributes.addFlashAttribute("success", "已保存 " + ad.getSubject());
            return "redirect:/admin/ads";
        } catch (Exception e) {
            model.addAttribute("error", e);
            model.addAttribute("ad", ad);
            return "admin/ad/form";
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable(value = "id") String id,
                         RedirectAttributes redirectAttributes) {
        try {
            //删除图片
            Ad ad = adService.getById(id);
            if (StringUtils.isNotBlank(ad.getImageKey())) {
                fileStorageService.delete(ad.getImageKey());
            }
            //删除广告
            adService.delete(id);
            redirectAttributes.addFlashAttribute("success", "已删除 id=" + id);
            return "redirect:/admin/ads";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败 id=" + id);
            return "redirect:/admin/ads";
        }
    }

    @RequestMapping(value = "enable/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String enable(@PathVariable(value = "id") String id) {
        adService.updateEnabled(id, true);
        return "ok";
    }

    @RequestMapping(value = "disable/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String disable(@PathVariable(value = "id") String id) {
        adService.updateEnabled(id, false);
        return "ok";
    }

    @ModelAttribute
    public void getUser(@RequestParam(value = "id", required = false) String id, Model model) {
        if (id != null) {
            model.addAttribute("ad", adService.getById(id));
        }
    }
}
