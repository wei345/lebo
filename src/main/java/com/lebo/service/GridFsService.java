package com.lebo.service;

import com.lebo.service.param.FileInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author: Wei Liu
 * Date: 13-7-8
 * Time: AM11:28
 */
@Service
public class GridFsService extends MongoService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    public static final String GRID_FS_FILES_COLLECTION_NAME = "fs.files";

    public String save(InputStream in, String filename, String contentType) throws IOException {
        if (!in.markSupported()) {
            in = new ByteArrayInputStream(IOUtils.toByteArray(in));
        }

        in.mark(Integer.MAX_VALUE);
        String md5 = Encodes.encodeHex(Digests.md5(in));
        GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("md5").is(md5)));

        if (file == null) {
            in.reset();
            GridFSFile gridFSFile = gridFsTemplate.store(in, filename, contentType);
            throwOnMongoError();
            return gridFSFile.getId().toString();
        } else {
            return file.getId().toString();
        }
    }

    public FileInfo getFileInfo(String id) {
        GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))));

        FileInfo fileInfo = new FileInfo();
        fileInfo.setContent(file.getInputStream());
        fileInfo.setFilename(file.getFilename());
        fileInfo.setLength((int) file.getLength());

        fileInfo.setLastModified(file.getUploadDate().getTime());
        fileInfo.setEtag("W/\"" + fileInfo.getLastModified() + "\"");

        fileInfo.setMimeType(file.getContentType());
        return fileInfo;
    }

    public void increaseReferrerCount(String id) {
        DBCollection collection = mongoTemplate.getCollection(GRID_FS_FILES_COLLECTION_NAME);
        DBObject q = new BasicDBObject("_id", new ObjectId(id));
        DBObject o = new BasicDBObject("$inc", new BasicDBObject("referrerCount", 1));
        collection.update(q, o);
    }

    public void decreaseReferrerCount(String id) {
        DBCollection collection = mongoTemplate.getCollection(GRID_FS_FILES_COLLECTION_NAME);
        DBObject q = new BasicDBObject("_id", new ObjectId(id));
        DBObject o = new BasicDBObject("$inc", new BasicDBObject("referrerCount", -1));
        collection.update(q, o);
    }

}
