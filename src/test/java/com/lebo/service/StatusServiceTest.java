package com.lebo.service;

import com.google.common.collect.Maps;
import com.lebo.SpringContextTestCase;
import com.lebo.entity.FileInfo;
import com.lebo.entity.Post;
import com.lebo.service.param.StatusFilterParam;
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
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

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
        Map<String, Object> meta = Maps.newHashMap();
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
        FileInfo fileInfo = new FileInfo(classPathResource.getInputStream(), contentType, classPathResource.getFile().length());
        FileInfo fileInfo1 = new FileInfo(classPathResource.getInputStream(), contentType, classPathResource.getFile().length());
        Post post = statusService.createPost("51d3221a1a883ebc140f7284", "测试发布视频", Arrays.asList(fileInfo, fileInfo1), null, null);

        assertNotNull(post);
    }

    @Test
    public void countUserStatus() {
        int count = statusService.countUserStatus("51def1e61a883914869e46f3");
        System.out.println(count);
    }

    @Test
    public void mentionNames() {
        LinkedHashSet<String> names = statusService.mentionScreenNames("@@abc@@ @@def", true);
        assertEquals(2, names.size());
        assertTrue(names.contains("abc"));
        assertTrue(names.contains("def"));

        names = statusService.mentionScreenNames("@abc@bcd @efghijk", true);
        assertEquals(3, names.size());
        assertTrue(names.contains("abc"));
        assertTrue(names.contains("bcd"));
        assertTrue(names.contains("efghijk"));
    }

    @Test
    public void findTags() {
        LinkedHashSet<String> tags = statusService.findHashtags("##a#b#c##d##", true);
        assertEquals(3, tags.size());
        assertTrue(tags.contains("a"));
        assertTrue(tags.contains("c"));
        assertTrue(tags.contains("d"));

        tags = statusService.findHashtags("#### ## ####", false);
        assertEquals(0, tags.size());
    }

    @Test
    public void filterPosts() {
        StatusFilterParam param = new StatusFilterParam();
        param.setTrack("杨过,标签1 2");
        List<Post> posts = statusService.filterPosts(param);
        assertTrue(posts.size() > 0);
        assertTrue(posts.get(0).getText().contains("杨过"));

        param.setFollow("51def1ce1a883914869e46f2,51def1e61a883914869e46f3");
        param.setTrack("@杨过, 标签");
        posts = statusService.filterPosts(param);
        assertTrue(posts.size() > 0);
        assertTrue(posts.get(0).getText().contains("@杨过"));

        param.setFollow("51def1e61a883914869e46f3");
        param.setTrack("");
        posts = statusService.filterPosts(param);
        assertTrue(posts.size() > 0);
        assertEquals("51def1e61a883914869e46f3", posts.get(0).getUserId());
    }

    @Test
    public void isHashtagOrAtSomeone() {
        assertTrue(statusService.isHashtagOrAtSomeone("@郭靖"));
        assertFalse(statusService.isHashtagOrAtSomeone("@郭靖@"));
        assertTrue(statusService.isHashtagOrAtSomeone("#华山论剑#"));
        assertFalse(statusService.isHashtagOrAtSomeone("#华山论剑"));
    }

    @Test
    public void dateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        Date date = new Date(1374466138948L);
        assertEquals("Mon Jul 22 12:08:58 +0800 2013", dateFormat.format(date));

        dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss ZZZZZ yyyy", Locale.CHINA);
        assertEquals("星期一 七月 22 12:08:58 +0800 2013", dateFormat.format(date));

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ", Locale.CHINA);
        assertEquals("2013-07-22 12:08:58 +0800", dateFormat.format(date));
    }

    @Test
    public void isReposted() {
        assertTrue(statusService.isReposted("51dfd3d21a8855744379891f", statusService.getPost("51df9f8d1a8899de19ebe351")));
        assertFalse(statusService.isReposted("51dfd3d21a8855744379891f", statusService.getPost("51ee22a01a88ab951db570c6")));
    }

    @Test
    public void refreshHotPosts() {
        statusService.refreshHotPosts();
    }

}
