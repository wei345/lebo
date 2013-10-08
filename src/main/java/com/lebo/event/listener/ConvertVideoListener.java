package com.lebo.event.listener;

import com.google.common.eventbus.Subscribe;
import com.lebo.event.AfterCommentCreateEvent;
import com.lebo.event.AfterPostCreateEvent;
import com.lebo.jms.ConvertVideoMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Wei Liu
 * Date: 13-10-8
 * Time: PM4:07
 */
@Component
public class ConvertVideoListener {
    @Autowired
    private ConvertVideoMessageProducer convertVideoMessageProducer;

    @Subscribe
    public void converPostVideo(AfterPostCreateEvent event) {
        if (event.getPost().getOriginPostId() == null && event.getPost().getVideo() != null) {
            convertVideoMessageProducer.sendQueue(ConvertVideoMessageProducer.OBJECT_TYPE_POST, event.getPost().getId());
        }
    }

    @Subscribe
    public void converCommentVideo(AfterCommentCreateEvent event) {
        if (event.getComment().getVideo() != null) {
            convertVideoMessageProducer.sendQueue(ConvertVideoMessageProducer.OBJECT_TYPE_COMMENT, event.getComment().getId());
        }
    }
}
