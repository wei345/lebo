package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.event.AfterCreatePostEvent;
import com.lebo.service.HashtagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Wei Liu
 * Date: 13-7-31
 * Time: PM4:05
 */
@Component
public class HashtagRecorder {

    @Autowired
    private HashtagService hashtagService;

    @Subscribe
    public void saveHashtags(AfterCreatePostEvent event){
         for(String name : event.getPost().getHashtags()){
             hashtagService.increaseCount(StringUtils.strip(name, "#"));
         }
    }

}
