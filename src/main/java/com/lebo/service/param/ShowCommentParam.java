package com.lebo.service.param;

import org.hibernate.validator.constraints.NotBlank;

public class ShowCommentParam extends PaginationParam {
    private String postId;

    @NotBlank
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
