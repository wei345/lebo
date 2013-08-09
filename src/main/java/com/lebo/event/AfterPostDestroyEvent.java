package com.lebo.event;

import com.lebo.entity.Post;

/**
 * @author: Wei Liu
 * Date: 13-7-29
 * Time: AM11:01
 */
public class AfterPostDestroyEvent {
    private Post post;

    public AfterPostDestroyEvent(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }
}
