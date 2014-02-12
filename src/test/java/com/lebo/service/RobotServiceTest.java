package com.lebo.service;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Wei Liu
 * Date: 14-1-2
 * Time: PM6:33
 */
public class RobotServiceTest extends SpringContextTestCase {

    @Autowired
    private RobotService robotService;

    @Test
    public void getGroups() throws Exception {
        System.out.println(robotService.getGroups());
        System.out.println(robotService.getGroups());
    }

    @Test
    public void renameGroup(){
        robotService.renameGroup("b", "bb");
    }

    @Test
    public void getRobotsByGroup(){
        System.out.println(robotService.getRobotsByGroup("aa").size());
    }
}
