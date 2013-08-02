package com.lebo.event;

import com.lebo.entity.Post;

/**
 * @author: Wei Liu
 * Date: 13-7-29
 * Time: AM11:01
 */
public class AfterDestroyPostEvent {
    private Post post;

    public AfterDestroyPostEvent(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }
}
