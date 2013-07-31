package com.lebo.service.param;

import org.hibernate.validator.constraints.NotBlank;

public class CommentListParam extends PaginationParam {
    private String postId;
    private Boolean hasVideo;

    @NotBlank
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Boolean getHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(Boolean hasVideo) {
        this.hasVideo = hasVideo;
    }
}
