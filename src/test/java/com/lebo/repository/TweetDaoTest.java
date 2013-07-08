package com.lebo.repository;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.Tweet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author: Wei Liu
 * Date: 13-7-8
 * Time: PM5:44
 */
public class TweetDaoTest extends SpringContextTestCase {
    @Autowired
    private TweetDao tweetDao;

    @Test
    public void findByUserId() {
        Pageable pageRequest = new PageRequest(0, 2, new Sort(new Sort.Order(Sort.Direction.ASC, "_id")));
        Page<Tweet> page = tweetDao.findByUserId("51da5bd71a881f1acdcb6308", pageRequest);
        assertTrue(page.hasNextPage());
        assertEquals(2, page.getTotalPages());

        //验证ID顺序
        assertTrue(page.getContent().get(0).getId().compareTo(page.getContent().get(1).getId()) < 0);
    }

    @Test
    public void findByUserIdAndIdLessThanEquals() {
        Pageable pageRequest = new PageRequest(0, 2, new Sort(new Sort.Order(Sort.Direction.DESC, "_id")));
        Page<Tweet> page = tweetDao.findByUserIdAndIdLessThanEquals("51da5bd71a881f1acdcb6308",
                "51da68381a88c135cf1f39de","000000000000000000000000", pageRequest);

        assertTrue(page.hasNextPage());
        assertEquals(2, page.getTotalPages());
        //验证ID顺序
        assertTrue(page.getContent().get(0).getId().compareTo(page.getContent().get(1).getId()) > 0);
    }

    /*@Test
    public void findByScreenName() {
        Pageable pageRequest = new PageRequest(0, 2, new Sort(new Sort.Order(Sort.Direction.ASC, "_id")));
        Page<Tweet> page = tweetDao.findByScreenName("51da5bd71a881f1acdcb6308", pageRequest);
        assertTrue(page.hasNextPage());
        assertEquals(2, page.getTotalPages());

        //验证ID顺序
        assertTrue(page.getContent().get(0).getId().compareTo(page.getContent().get(1).getId()) < 0);
    }

    @Test
    public void findByScreenNameAndIdLessThanEquals() {
        Pageable pageRequest = new PageRequest(0, 2, new Sort(new Sort.Order(Sort.Direction.DESC, "_id")));
        Page<Tweet> page = tweetDao.findByUserIdAndIdLessThanEquals("51da5bd71a881f1acdcb6308",
                "51da68381a88c135cf1f39de", pageRequest);

        assertTrue(page.hasNextPage());
        assertEquals(2, page.getTotalPages());
        //验证ID顺序
        assertTrue(page.getContent().get(0).getId().compareTo(page.getContent().get(1).getId()) > 0);
    }*/

}
