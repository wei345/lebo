package com.lebo.rest;

import com.lebo.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Wei Liu
 * Date: 13-7-12
 * Time: PM10:20
 */
@Controller
@RequestMapping("/api/1/tags")
public class TagRestController {

    @Autowired
    private StatusService statusService;

    /**
     * 搜索Post.text中出现的标签，按标签出现次数由大到小排序，返回最多100个标签。
     */
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public Object search(@RequestParam("q") String q,
                         @RequestParam(value = "count", defaultValue = "20") int count) {
        if (count > 100) count = 100;
        return statusService.searchTags(q, count);
    }
}
