package com.lebo.service;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.LinkedHashSet;
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
        LinkedHashSet<String> names = mentionService.mentionNames("@@abc@@ @@def");
        assertEquals(2, names.size());
        assertTrue(names.contains("abc"));
        assertTrue(names.contains("def"));

        names = mentionService.mentionNames("@abc@bcd @efghijk");
        assertEquals(3, names.size());
        assertTrue(names.contains("abc"));
        assertTrue(names.contains("bcd"));
        assertTrue(names.contains("efghijk"));
    }

    @Test
    public void findTags(){
        LinkedHashSet<String> tags = mentionService.findTags("##a#b#c##d##");
        assertEquals(3, tags.size());
        assertTrue(tags.contains("a"));
        assertTrue(tags.contains("c"));
        assertTrue(tags.contains("d"));

        tags = mentionService.findTags("#### ## ####");
        assertEquals(0, tags.size());
    }
}
