package com.lebo.service;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashSet;

import static com.mongodb.util.MyAsserts.assertTrue;

/**
 * @author: Wei Liu
 * Date: 13-7-15
 * Time: PM2:02
 */
public class SegmentationTest extends SpringContextTestCase{

    @Autowired
    private Segmentation segmentation;

    @Test
    public void findWords(){
        LinkedHashSet words = segmentation.findWords("今天下雨了");
        assertTrue(words.contains("今天"));
        assertTrue(words.contains("下雨"));
    }
}
