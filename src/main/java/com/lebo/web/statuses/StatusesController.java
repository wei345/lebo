package com.lebo.web.statuses;

import com.lebo.entity.Post;
import com.lebo.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: Wei Liu
 * Date: 13-8-17
 * Time: PM4:15
 */
@RequestMapping("/statuses")
@Controller
public class StatusesController {

    @Autowired
    private StatusService statusService;

    @RequestMapping(value = "show", method = RequestMethod.GET)
    public String show(@RequestParam("id") String id, Model model){
        Post post = statusService.getPost(id);
        model.addAttribute("post", statusService.toBasicStatusDto(post));
        return "statuses/show";
    }
}
