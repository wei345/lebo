package com.lebo.rest;

import com.lebo.entity.Hashtag;
import com.lebo.service.HashtagService;
import com.lebo.service.param.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/api/1/hashtags")
public class HashtagRestController {

    @Autowired
    private HashtagService hashtagService;

    /**
     * 搜索Post.text中出现的标签，按标签出现次数由大到小排序。
     */
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public Object search(@Valid SearchParam param) {
        param.setSort(new Sort(Sort.Direction.DESC, Hashtag.POSTS_COUNT_KEY));
        return hashtagService.toChannelDtos(hashtagService.searchHashtags(param));
    }
}
