package com.lebo.service;

import com.mongodb.*;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import java.io.ByteArrayInputStream;
import java.io.File;
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

    public ContentInfo getContentInfo(String id){
        GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))));

        GridFsService.ContentInfo contentInfo = new GridFsService.ContentInfo();
        contentInfo.content = file.getInputStream();
        contentInfo.fileName = file.getFilename();
        contentInfo.length = (int) file.getLength();

        contentInfo.lastModified = file.getUploadDate().getTime();
        contentInfo.etag = "W/\"" + contentInfo.lastModified + "\"";

        contentInfo.mimeType = file.getContentType();
        return contentInfo;
    }

    public void increaseReferrerCount(String id){
        DBCollection collection =  mongoTemplate.getCollection(GRID_FS_FILES_COLLECTION_NAME);
        DBObject q = new BasicDBObject("_id", new ObjectId(id));
        DBObject o = new BasicDBObject("$inc", new BasicDBObject("referrerCount", 1));
        collection.update(q, o);
    }

    public void decreaseReferrerCount(String id){
        DBCollection collection =  mongoTemplate.getCollection(GRID_FS_FILES_COLLECTION_NAME);
        DBObject q = new BasicDBObject("_id", new ObjectId(id));
        DBObject o = new BasicDBObject("$inc", new BasicDBObject("referrerCount", -1));
        collection.update(q, o);
    }

    /**
     * 定义Content的基本信息.
     */
    public static class ContentInfo {
        protected InputStream content;
        protected String fileName;
        protected int length;
        protected String mimeType;
        protected long lastModified;
        protected String etag;

        public InputStream getContent() {
            return content;
        }

        public String getFileName() {
            return fileName;
        }

        public int getLength() {
            return length;
        }

        public String getMimeType() {
            return mimeType;
        }

        public long getLastModified() {
            return lastModified;
        }

        public String getEtag() {
            return etag;
        }
    }
}
