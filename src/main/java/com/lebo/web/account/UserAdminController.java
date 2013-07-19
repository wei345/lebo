package com.lebo.web.account;

import com.lebo.entity.User;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * 管理员管理用户的Controller.
 *
 * @author Wei Liu
 */
@Controller
@RequestMapping(value = "/admin/user")
public class UserAdminController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(SearchParam param, Model model) {
        Page<User> page = accountService.searchUser(param);
        model.addAttribute("page", page);
        model.addAttribute("q", param.getQ());

        return "account/adminUserList";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("user", accountService.getUser(id));
        return "account/adminUserForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        accountService.saveUser(user, null);
        redirectAttributes.addFlashAttribute("message", "更新用户" + user.getScreenName() + "成功");
        return "redirect:/admin/user";
    }

    //TODO 删除用户功能
	/*@RequestMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
		User user = accountService.getUser(id);
		accountService.deleteUser(id);
		redirectAttributes.addFlashAttribute("message", "删除用户" + user.getLoginName() + "成功");
		return "redirect:/admin/user";
	}*/

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
}
