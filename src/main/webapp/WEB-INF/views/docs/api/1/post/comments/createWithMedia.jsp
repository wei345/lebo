<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="发布评论" method="POST" action="${ctx}/api/1/comments/createWithMedia.json" enctype="multipart/form-data">
    <ul>
        <li>回复帖子：需要 postId (被回复的帖子id)</li>
        <li>回复评论：需要 replyCommentId (被回复的评论id) 和 postId</li>
    </ul>
    <tags:field name="postId" value="51de1c3c1a88b82f949585b4" optional="true"/>
    <tags:field name="replyCommentId" value="" optional="true"/>
    <ul>
        <li>视频评论：需要 video 和 image</li>
        <li>语音评论：需要 audio</li>
    </ul>
    <tags:field name="video" type="file" optional="true"/>
    <tags:field name="image" type="file" optional="true"/>
    <tags:field name="audio" type="file" optional="true"/>
    <tags:textarea name="text" value="视频评论" optional="true"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1/comments/createWithMedia.json">
    {
        id: "52245b3b1a880df943893ebb",
        postId: "5221f1a91a88cad717f90245",
        createdAt: "Mon Sep 02 17:32:43 +0800 2013",
        text: "语音评论",
        files: [ ],
        audio: {
            length: 346470,
            contentType: "audio/amr",
            contentUrl: "http://file.dev.lebooo.com/comment/52245b3b1a880df943893ebb-audio-346470.amr"
        },
        hasVideo: false,
        user: {
            id: "5216d0dc1a8829c4ae1bbec3",
            screenName: "涛涛_IT",
            profileImageUrl: "http://file.dev.lebooo.com/5216d0dc1a8829c4ae1bbec4.png",
            profileImageBiggerUrl: "http://file.dev.lebooo.com/5216d0dd1a8829c4ae1bbec5.png",
            profileImageOriginalUrl: "http://file.dev.lebooo.com/5216d0de1a8829c4ae1bbec6.png",
            createdAt: "Fri Aug 23 11:02:52 +0800 2013",
            followersCount: 0,
            friendsCount: 0,
            statusesCount: 3,
            favoritesCount: 0,
            beFavoritedCount: 0,
            viewCount: 0,
            weiboVerified: false,
            blocking: false
        }
    }
</tags:example>