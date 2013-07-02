package com.lebo.service.lebo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author: Wei Liu
 * Date: 13-7-2
 * Time: PM4:32
 */
@Service
public class LeboService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public void publishLebo(File video, String description){
        //gridFsTemplate.store()
    }
}
