package com.lebo.service;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Wei Liu
 * Date: 13-8-1
 * Time: AM11:40
 */
public class CommentServiceTest extends SpringContextTestCase {
    @Autowired
    private CommentService commentService;

    @Test
    public void deleteByPostId() {
        commentService.deleteByPostId("51ef6f741a8847c724a187f7");
    }
}
