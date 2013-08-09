package com.lebo.event;

import com.lebo.entity.Post;

/**
 * @author: Wei Liu
 * Date: 13-7-29
 * Time: AM11:10
 */
public class AfterPostViewEvent {
    private Post post;

    public AfterPostViewEvent(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }
}
