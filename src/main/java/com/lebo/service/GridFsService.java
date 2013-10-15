package com.lebo.service;

import com.lebo.entity.FileInfo;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author: Wei Liu
 * Date: 13-7-8
 * Time: AM11:28
 */
public class GridFsService extends AbstractMongoService implements FileStorageService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    public static final String GRID_FS_FILES_COLLECTION_NAME = "fs.files";
    public static final String MD5_KEY = "md5";
    public static final String REFERRER_COUNT_KEY = "referrersCount";

    /**
     * @throws IOException
     * @throws DuplicateException 当文件重复时
     */
    @Override
    public String save(FileInfo fileInfo) {
        InputStream in = null;
        try {
            in = fileInfo.getContent();
            if (!fileInfo.getContent().markSupported()) {
                in = new ByteArrayInputStream(IOUtils.toByteArray(fileInfo.getContent()));
                IOUtils.closeQuietly(fileInfo.getContent());
            }

            in.mark(Integer.MAX_VALUE);
            String md5 = Encodes.encodeHex(Digests.md5(in));
            GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where(MD5_KEY).is(md5)));

            if (file == null) {
                in.reset();
                GridFSFile gridFSFile = gridFsTemplate.store(in, "filename", fileInfo.getContentType());
                throwOnMongoError();
                increaseReferrerCount(gridFSFile.getId().toString());
                fileInfo.setKey(gridFSFile.getId().toString());
                return fileInfo.getKey();
            } else {
                increaseReferrerCount(file.getId().toString());
                fileInfo.setKey(file.getId().toString());
                return fileInfo.getKey();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                IOUtils.closeQuietly(in);
            }
        }
    }

    @Override
    public void increaseReferrerCount(String id) {
        if (isMongoId(id)) {
            mongoTemplate.updateFirst(new Query(new Criteria("_id").is(new ObjectId(id))),
                    new Update().inc(REFERRER_COUNT_KEY, 1), GRID_FS_FILES_COLLECTION_NAME);
        }
    }

    @Override
    public void decreaseReferrerCount(String id) {
        if (isMongoId(id)) {
            mongoTemplate.updateFirst(new Query(new Criteria("_id").is(new ObjectId(id))),
                    new Update().inc(REFERRER_COUNT_KEY, -1), GRID_FS_FILES_COLLECTION_NAME);
        }
    }

    @Override
    public FileInfo get(String id) {
        GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))));

        FileInfo fileInfo = new FileInfo();
        fileInfo.setContent(file.getInputStream());
        fileInfo.setFilename(file.getFilename());
        fileInfo.setLength((int) file.getLength());

        fileInfo.setLastModified(file.getUploadDate().getTime());
        fileInfo.seteTag("W/\"" + fileInfo.getLastModified() + "\"");

        fileInfo.setContentType(file.getContentType());
        return fileInfo;
    }

    /*public void increaseViewCount(String id) {
        DBCollection collection = mongoTemplate.getCollection(GRID_FS_FILES_COLLECTION_NAME);
        DBObject q = new BasicDBObject("_id", new ObjectId(id));
        DBObject o = new BasicDBObject("$inc", new BasicDBObject("viewCount", 1));
        collection.update(q, o);
    }*/

    /**
     * 减少由id参数指定的文件的引用数，如果引用数为0，则删除文件。
     *
     * @param id 文件id
     */
    @Override
    public void delete(String id) {
        if (isMongoId(id)) {
            decreaseReferrerCount(id);
            gridFsTemplate.delete(new Query(new Criteria("_id").is(new ObjectId(id)).and(REFERRER_COUNT_KEY).lte(0)));
        }
    }

    @Override
    public List<String> save(FileInfo... fileInfos) {
        List<String> fileIds = new ArrayList<String>(fileInfos.length);
        try {
            for (FileInfo fileInfo : fileInfos) {
                fileIds.add(save(fileInfo));
            }
            return fileIds;
        } catch (Exception e) {
            for (String fileId : fileIds) {
                delete(fileId);
            }
            throw new ServiceException("存储文件失败", e);
        }
    }

    @Override
    public String getContentUrl(String fileId, String suffix) {
        if (isMongoId(fileId)) {
            String contentUrl = "/files/" + fileId;
            if (StringUtils.isNotBlank(suffix)) {
                contentUrl += suffix;
            }
            return contentUrl;
        }
        return fileId;
    }
}
