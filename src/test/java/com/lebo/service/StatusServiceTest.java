package com.lebo.service;

import com.google.common.collect.Maps;
import com.lebo.SpringContextTestCase;
import com.lebo.entity.Post;
import com.lebo.service.param.FileInfo;
import com.mongodb.gridfs.GridFSFile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import javax.activation.FileTypeMap;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * @author: Wei Liu
 * Date: 13-7-2
 * Time: PM4:46
 */
public class StatusServiceTest extends SpringContextTestCase {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StatusService statusService;

    @Test
    public void store() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("applicationContext.xml");
        assertTrue(classPathResource.exists());

        long fileCount = mongoTemplate.getCollection("fs.files").count();
        Map meta = Maps.newHashMap();
        meta.put("a", 1);
        meta.put("b", "abc");

        GridFSFile file = gridFsTemplate.store(classPathResource.getInputStream(), classPathResource.getFilename(), "xml", meta);
        assertTrue(fileCount + 1 == mongoTemplate.getCollection("fs.files").count());

        assertEquals(meta.get("b"), file.getMetaData().get("b"));

        gridFsTemplate.delete(new Query(new Criteria("_id").is(file.get("_id"))));
        assertTrue(fileCount == mongoTemplate.getCollection("fs.files").count());
    }

    @Test
    public void update() throws Exception {
        ClassPathResource classPathResource = new ClassPathResource("applicationContext.xml");
        assertTrue(classPathResource.exists());

        String contentType = FileTypeMap.getDefaultFileTypeMap().getContentType(classPathResource.getFile());
        FileInfo fileInfo = new FileInfo(classPathResource.getInputStream(), classPathResource.getFilename(), contentType);
        Post post = statusService.update("51d3221a1a883ebc140f7284", "测试发布视频", Arrays.asList(fileInfo), null, "test");

        assertNotNull(post);
    }

    @Test
    public void countUserStatus() {
        int count = statusService.countUserStatus("51def1e61a883914869e46f3");
        System.out.println(count);
    }

    @Test
    public void findAllTags() {
        statusService.findAllHashtags();
    }

    @Test
    public void mentionNames() {
        LinkedHashSet<String> names = statusService.mentionScreenNames("@@abc@@ @@def");
        assertEquals(2, names.size());
        assertTrue(names.contains("abc"));
        assertTrue(names.contains("def"));

        names = statusService.mentionScreenNames("@abc@bcd @efghijk");
        assertEquals(3, names.size());
        assertTrue(names.contains("abc"));
        assertTrue(names.contains("bcd"));
        assertTrue(names.contains("efghijk"));
    }

    @Test
    public void findTags() {
        LinkedHashSet<String> tags = statusService.findHashtags("##a#b#c##d##");
        assertEquals(3, tags.size());
        assertTrue(tags.contains("a"));
        assertTrue(tags.contains("c"));
        assertTrue(tags.contains("d"));

        tags = statusService.findHashtags("#### ## ####");
        assertEquals(0, tags.size());
    }
}
