package com.lebo.event;

import com.lebo.entity.Comment;

/**
 * @author: Wei Liu
 * Date: 13-8-9
 * Time: AM10:57
 */
public class AfterCommentDestroyEvent {
    private Comment comment;
    public AfterCommentDestroyEvent(Comment comment){
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }
}
