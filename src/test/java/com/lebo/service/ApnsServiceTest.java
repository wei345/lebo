package com.lebo.service;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Wei Liu
 * Date: 13-8-7
 * Time: PM4:51
 */
public class ApnsServiceTest extends SpringContextTestCase {
    @Autowired
    private ApnsService apnsService;

    @Test
    public void pushMessage() {
        //开发 77bb0c0de991ad0e166931442be3615b448ad02a0d98d3dd043d7429ca76cc96
        //生产 45aac5bfb2e7ad434e881f3f436226dfca63b79f55f330abd7e5f3909241142a
        apnsService.pushMessage("test4", 4, "45aac5bfb2e7ad434e881f3f436226dfca63b79f55f330abd7e5f3909241142a");
    }
}
