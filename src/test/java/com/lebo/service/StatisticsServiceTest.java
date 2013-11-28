package com.lebo.service;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.Statistics;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.mapper.JsonMapper;

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

    @Test
    public void createDaily(){
        Statistics statistics = statisticsService.createDaily(2013, 11, 15);
        JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
        System.out.println(jsonMapper.toJson(statistics));
    }
}
