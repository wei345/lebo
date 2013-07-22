package com.lebo.web.option;

import com.lebo.entity.Option;
import com.lebo.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author: Wei Liu
 * Date: 13-7-22
 * Time: PM1:43
 */
@Controller
@RequestMapping("/admin/options")
public class OptionController {

    @Autowired
    private OptionService optionService;

    @RequestMapping(method = RequestMethod.GET)
    public String optionForm(Model model) {
        Option option = optionService.getOption();
        model.addAttribute("option", option);
        return "option/optionForm";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveOption(@ModelAttribute("option") Option option,
                             RedirectAttributes redirectAttributes) {
        optionService.saveOption(option);
        redirectAttributes.addFlashAttribute("success", "保存成功");
        return "redirect:/admin/options";
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute()
    public void getOption(Model model) {
        model.addAttribute("option", optionService.getOption());
    }
}
