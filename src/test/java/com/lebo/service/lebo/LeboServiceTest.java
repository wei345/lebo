package com.lebo.service.lebo;

import com.google.common.collect.Maps;
import com.lebo.SpringContextTestCase;
import com.mongodb.gridfs.GridFSFile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author: Wei Liu
 * Date: 13-7-2
 * Time: PM4:46
 */
public class LeboServiceTest extends SpringContextTestCase{
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void store() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("applicationContext.xml");
        assertTrue(classPathResource.exists());

        long fileCount = mongoTemplate.getCollection("fs.files").count();
        Map meta = Maps.newHashMap();
        meta.put("a",1);
        meta.put("b", "abc");

        GridFSFile file = gridFsTemplate.store(classPathResource.getInputStream(), classPathResource.getFilename(), "xml", meta);
        assertTrue(fileCount + 1 == mongoTemplate.getCollection("fs.files").count());

        assertEquals(meta.get("b"), file.getMetaData().get("b"));

        gridFsTemplate.delete(new Query(new Criteria("_id").is(file.get("_id"))));
        assertTrue(fileCount == mongoTemplate.getCollection("fs.files").count());
    }
}
