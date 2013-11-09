package com.lebo.web.admin;

import com.lebo.entity.FileInfo;
import com.lebo.entity.RecommendedApp;
import com.lebo.service.AbstractMongoService;
import com.lebo.service.FileStorageService;
import com.lebo.service.SettingService;
import com.lebo.web.ControllerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * 管理推荐应用。
 *
 * @author: Wei Liu
 * Date: 13-10-15
 * Time: PM2:14
 */
@RequestMapping("/admin/recommendedApps")
@Controller
public class RecommendedAppController {
    private Logger logger = LoggerFactory.getLogger(RecommendedAppController.class);
    @Autowired
    private SettingService settingService;
    @Autowired
    private FileStorageService fileStorageService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("apps", settingService.getAllRecommendedApp());
        return "admin/setting/recommendedAppList";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm() {
        return "admin/setting/recommendedAppForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("app") RecommendedApp recommendedApp,
                         @RequestParam(value = "image", required = false) MultipartFile image,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            if (image != null) {
                //保存新文件
                FileInfo fileInfo = ControllerUtils.getFileInfo(image);
                fileInfo.setKey(AbstractMongoService.generateFileId("images/recommendedApps", null, recommendedApp.getImageSlug(), fileInfo.getLength(), fileInfo.getContentType(), fileInfo.getFilename()));
                fileStorageService.save(fileInfo);

                //删除旧文件
                if (recommendedApp.getImageKey() != null && !recommendedApp.getImageKey().equals(fileInfo.getKey())) {
                    fileStorageService.delete(recommendedApp.getImageKey());
                }

                recommendedApp.setImageKey(fileInfo.getKey());
            }
            settingService.saveRecommendedApp(recommendedApp);
            redirectAttributes.addFlashAttribute("success", "已保存 " + recommendedApp.getName());
            return "redirect:/admin/recommendedApps";
        } catch (Exception e) {
            model.addAttribute("error", e);
            model.addAttribute("app", recommendedApp);
            return "admin/setting/recommendedAppForm";
        }
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("app", settingService.getRecommendedApp(id));
        return "admin/setting/recommendedAppForm";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable(value = "id") String id,
                         RedirectAttributes redirectAttributes) {
        try {
            //删除图片
            RecommendedApp app = settingService.getRecommendedApp(id);
            if (StringUtils.isNotBlank(app.getImageKey())) {
                fileStorageService.delete(app.getImageKey());
            }
            //删除频道
            settingService.deleteRecommendedApp(id);
            redirectAttributes.addFlashAttribute("success", String.format("已删除 id=%s, name=%s", id, app.getName()));
            return "redirect:/admin/recommendedApps";
        } catch (Exception e) {
            logger.warn("删除推荐应用失败, id=" + id, e);
            redirectAttributes.addFlashAttribute("error", "删除失败 id=" + id);
            return "redirect:/admin/recommendedApps";
        }
    }

    @RequestMapping(value = "enable/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String enable(@PathVariable(value = "id") String id) {
        settingService.updateRecommendedAppEnabled(id, true);
        return "ok";
    }

    @RequestMapping(value = "disable/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String disable(@PathVariable(value = "id") String id) {
        settingService.updateRecommendedAppEnabled(id, false);
        return "ok";
    }

    @ModelAttribute
    public void getUser(@RequestParam(value = "id", required = false) String id, Model model) {
        if (id != null) {
            model.addAttribute("app", settingService.getRecommendedApp(id));
        }
    }
}
