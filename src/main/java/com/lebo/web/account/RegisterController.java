package com.lebo.web.account;

import com.lebo.entity.User;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * 用户注册的Controller.
 *
 * @author Wei Liu
 */
@Controller
@RequestMapping(value = "/register")
public class RegisterController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String registerForm() {
        return "account/register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String register(@Valid User user, RedirectAttributes redirectAttributes) {
        accountService.registerUser(user);
        redirectAttributes.addFlashAttribute("username", user.getEmail());
        return "redirect:/login";
    }

    /**
     * Ajax请求校验loginName是否唯一。
     */
    @RequestMapping(value = "checkScreenName")
    @ResponseBody
    public String checkLoginName(@RequestParam("screenName") String screenName) {
        if (accountService.findUserByScreenName(screenName) == null) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * Ajax请求校验checkEmail是否唯一。
     */
    @RequestMapping(value = "checkEmail")
    @ResponseBody
    public String checkEmail(@RequestParam("email") String email) {
        if (accountService.findUserByEmail(email) == null) {
            return "true";
        } else {
            return "false";
        }
    }
}
