package com.lebo.event;

import com.lebo.entity.Post;

/**
 * @author: Wei Liu
 * Date: 13-7-29
 * Time: AM10:59
 */
public class AfterPostCreateEvent {
    private Post post;

    public AfterPostCreateEvent(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }
}
