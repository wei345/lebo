package com.lebo.service;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Wei Liu
 * Date: 13-8-17
 * Time: PM12:30
 */
public class ALiYunStorageServiceTest extends SpringContextTestCase {
    @Autowired
    private ALiYunStorageService aLiYunStorageService;

    @Test
    public void key() {
        System.out.println(aLiYunStorageService.key());
    }
}
