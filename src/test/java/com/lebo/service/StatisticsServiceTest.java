package com.lebo.service;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.Statistics;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Wei Liu
 * Date: 13-10-25
 * Time: PM3:47
 */
public class StatisticsServiceTest extends SpringContextTestCase {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    public void create() throws Exception {
        Statistics statistics = statisticsService.create();
        System.out.println(statistics);
    }
}
