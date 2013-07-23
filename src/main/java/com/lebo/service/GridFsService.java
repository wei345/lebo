package com.lebo.service;

import com.lebo.rest.dto.StatusDto;
import com.lebo.service.param.FileInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.List;


/**
 * @author: Wei Liu
 * Date: 13-7-8
 * Time: AM11:28
 */
@Service
public class GridFsService extends AbstractMongoService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    public static final String GRID_FS_FILES_COLLECTION_NAME = "fs.files";

    /**
     * @throws IOException
     * @throws DuplicateException 当文件重复时
     */
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
            // 不存储同样文件
            throw new DuplicateException(String.format("File[filename=%s, contentType=%s] already exists.", filename, contentType));
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

    public void increaseViewCount(String id) {
        DBCollection collection = mongoTemplate.getCollection(GRID_FS_FILES_COLLECTION_NAME);
        DBObject q = new BasicDBObject("_id", new ObjectId(id));
        DBObject o = new BasicDBObject("$inc", new BasicDBObject("viewCount", 1));
        collection.update(q, o);
    }

    public void delete(String id) {
        gridFsTemplate.delete(new Query(new Criteria("_id").is(new ObjectId(id))));
    }

    public List<String> saveFilesSafely(List<FileInfo> fileInfos) {
        List<String> fileIds = new ArrayList<String>();
        try {
            for (FileInfo fileInfo : fileInfos) {
                fileIds.add(save(fileInfo.getContent(), fileInfo.getFilename(), fileInfo.getMimeType()));
            }
            return fileIds;
        } catch (Exception e) {
            for (String fileId : fileIds) {
                delete(fileId);
            }
            throw new ServiceException("存储文件失败", e);
        }
    }

    public String getContentUrl(String fileId, String suffix){
        if(isMongoId(fileId)){
            String contentUrl =  "/files/" + fileId;
            if(StringUtils.isNotBlank(suffix)){
                contentUrl += suffix;
            }
            return contentUrl;
        }
        return fileId;
    }

    public StatusDto.FileInfoDto getFileInfoDto(String id, String contentUrlSuffix) {
        GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))));

        StatusDto.FileInfoDto dto = new StatusDto.FileInfoDto();
        dto.setContentType(file.getContentType());
        dto.setContentUrl(getContentUrl(id, contentUrlSuffix));
        dto.setLength((int) file.getLength());
        dto.setFilename(file.getFilename());

        return dto;
    }
}
