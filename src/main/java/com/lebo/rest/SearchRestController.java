package com.lebo.rest;

import com.lebo.service.StatusService;
import com.lebo.service.param.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * @author: Wei Liu
 * Date: 13-7-12
 * Time: PM10:20
 */
@Controller
@RequestMapping("/api/v1/search")
public class SearchRestController {

    @Autowired
    private StatusService statusService;

    /**
     * 搜索Post.text中出现的标签，按标签出现次数由大到小排序，返回最多100个标签。
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "tags", method = RequestMethod.GET)
    @ResponseBody
    public Object searchTags(@Valid SearchParam param) {
        return statusService.toStatusDtoList(statusService.findPostsByTag(param));
    }
}
