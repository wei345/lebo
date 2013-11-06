package com.lebo.web.admin;

import com.lebo.entity.User;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PaginationParam;
import com.lebo.service.param.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

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
    public String list(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "page", defaultValue = "0") int pageNo,
                       @RequestParam(value = "size", defaultValue = PaginationParam.DEFAULT_COUNT + "") int size,
                       @RequestParam(value = "orderBy", defaultValue = "_id") String orderBy,
                       @RequestParam(value = "order", defaultValue = "desc") String order,
                       Model model) {
        Sort.Direction direction;
        if (order.equals("desc")) {
            direction = Sort.Direction.DESC;
        } else if (order.equals("asc")) {
            direction = Sort.Direction.ASC;
        } else {
            model.addAttribute("error", String.format("参数order值[%s]无效", order));
            return "admin/user/adminUserList";
        }

        //搜索
        SearchParam param = new SearchParam(q, pageNo, size, direction, orderBy);
        List<User> users = accountService.searchUser(param);

        model.addAttribute("users", users);

        model.addAttribute("q", param.getQ());
        model.addAttribute("page", param.getPageNumber());
        model.addAttribute("size", param.getPageSize());
        model.addAttribute("currentSize", users.size());
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("order", order);

        return "admin/user/adminUserList";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("user", accountService.getUser(id));
        return "admin/user/adminUserForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        accountService.saveUser(user);
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
