package com.lebo.repository;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.Post;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springside.modules.test.category.UnStable;

import static org.junit.Assert.*;

/**
 * @author: Wei Liu
 * Date: 13-7-8
 * Time: PM5:44
 */
public class PostDaoTest extends SpringContextTestCase implements UnStable {
    @Autowired
    private PostDao postDao;

    @Test
    public void findByUserIdAndOriginPostId() {
        Post post = postDao.findByUserIdAndOriginPostId("51e778ea1a8816dc79e40aaf", "51e3a0ca1a8890916e962c94");
        assertNotNull(post);
    }
}
