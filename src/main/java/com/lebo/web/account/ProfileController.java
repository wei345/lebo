package com.lebo.web.account;

import com.lebo.entity.User;
import com.lebo.service.account.AccountService;
import com.lebo.service.account.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * 用户修改自己资料的Controller.
 *
 * @author Wei Liu
 */
@Controller
@RequestMapping(value = "/profile")
public class ProfileController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String updateForm(Model model) {
        String id = accountService.getCurrentUserId();
        model.addAttribute("user", accountService.getUser(id));
        return "account/profile";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("user") User user) {
        accountService.saveUser(user);
        updateCurrentUserName(user.getName());
        return "redirect:/";
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getUser(@RequestParam(value = "id", required = false) String id, Model model) {
        if (id != null) {
            model.addAttribute("user", accountService.getUser(id));
        }
    }

    /**
     * 更新Shiro中当前用户的用户名.
     */
    private void updateCurrentUserName(String userName) {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        user.name = userName;
    }
}
