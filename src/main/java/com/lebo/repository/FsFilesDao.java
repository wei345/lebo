package com.lebo.repository;

import com.lebo.entity.FsFiles;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: Wei Liu
 * Date: 13-7-3
 * Time: AM11:21
 */
public interface FsFilesDao extends MongoRepository<FsFiles, String> {

    FsFiles findByMd5(String md5);
}
