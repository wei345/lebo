package com.lebo.service;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Wei Liu
 * Date: 13-10-24
 * Time: AM12:05
 */
public class VideoConvertServiceTest extends SpringContextTestCase {
    @Autowired
    private VideoConvertService videoConvertService;

    @Test
    public void converPostVideo() throws Exception {
        videoConvertService.converPostVideo("522474591a886ad3139c1d59");
    }
}
