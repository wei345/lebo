package com.lebo.service;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.Hashtag;
import com.lebo.service.param.SearchParam;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-31
 * Time: PM4:20
 */
public class HashtagServiceTest extends SpringContextTestCase {
    @Autowired
    private HashtagService hashtagService;

    @Test
    public void increaseCount() {
        hashtagService.increaseCount("测试");
    }

    @Test
    public void searchHashtags() {
        SearchParam p = new SearchParam();
        p.setSort(new Sort(Sort.Direction.DESC, Hashtag.COUNT_KEY));
        p.setQ("石");
        List<Hashtag> hashtags = hashtagService.searchHashtags(p);
        System.out.println(hashtags);
    }
}
