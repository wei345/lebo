package com.lebo.service;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-11
 * Time: PM6:39
 */
public class MentionServiceTest extends SpringContextTestCase{

    @Autowired
    private MentionService mentionService;

    @Test
    public void mentionNames(){
        List<String> nameList = mentionService.mentionNames("@@abc@@ @@def");
        assertEquals(2, nameList.size());
        assertTrue(nameList.contains("abc"));
        assertTrue(nameList.contains("def"));

        nameList = mentionService.mentionNames("@abc@bcd @efghijk");
        assertEquals(3, nameList.size());
        assertTrue(nameList.contains("abc"));
        assertTrue(nameList.contains("bcd"));
        assertTrue(nameList.contains("efghijk"));
    }
}
