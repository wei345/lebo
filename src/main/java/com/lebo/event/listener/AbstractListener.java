package com.lebo.event.listener;

import com.lebo.event.ApplicationEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: Wei Liu
 * Date: 13-7-29
 * Time: PM3:12
 */
@Component
public class AbstractListener {
    @Autowired
    protected ApplicationEventBus eventBus;

    @PostConstruct
    public void registerEvents() {
        eventBus.register(this);
    }
}
