package com.lebo.event;

import com.lebo.entity.Post;

/**
 * @author: Wei Liu
 * Date: 13-8-3
 * Time: PM1:52
 */
public class BeforeCreatePostEvent {
    private Post post;

    public BeforeCreatePostEvent(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }
}
