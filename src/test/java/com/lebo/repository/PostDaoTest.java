package com.lebo.repository;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.Post;
import com.lebo.service.param.SearchParam;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springside.modules.test.category.UnStable;

import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author: Wei Liu
 * Date: 13-7-8
 * Time: PM5:44
 */
public class PostDaoTest extends SpringContextTestCase implements UnStable {
    @Autowired
    private PostDao postDao;

    @Test
    public void findByUserId() {
        Pageable pageRequest = new PageRequest(0, 2, new Sort(new Sort.Order(Sort.Direction.ASC, "_id")));
        Page<Post> page = postDao.findByUserId("51da5bd71a881f1acdcb6308", pageRequest);
        assertTrue(page.hasNextPage());
        assertEquals(2, page.getTotalPages());

        //验证ID顺序
        assertTrue(page.getContent().get(0).getId().compareTo(page.getContent().get(1).getId()) < 0);
    }

    @Test
    public void findByUserIdAndIdLessThanEquals() {
        Pageable pageRequest = new PageRequest(0, 2, new Sort(new Sort.Order(Sort.Direction.DESC, "_id")));
        Page<Post> page = postDao.userTimeline("51da5bd71a881f1acdcb6308",
                "51da68381a88c135cf1f39de", "000000000000000000000000", pageRequest);

        assertTrue(page.hasNextPage());
        assertEquals(2, page.getTotalPages());
        //验证ID顺序
        assertTrue(page.getContent().get(0).getId().compareTo(page.getContent().get(1).getId()) > 0);
    }

    @Test
    public void search(){
        SearchParam param = new SearchParam();
        Page<Post> page = postDao.search("转发",
                param.getMaxId(), param.getSinceId(), param);

        assertTrue(page.getContent().size() > 0);
        assertTrue(page.getContent().get(0).getText().contains("转发"));
    }

}
