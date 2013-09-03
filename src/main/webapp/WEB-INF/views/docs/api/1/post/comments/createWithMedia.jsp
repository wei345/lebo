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
    <p>
        语音时长，整数
    </p>
    <tags:field name="audioDuration" optional="true"/>
    <tags:textarea name="text" value="视频评论" optional="true"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1/comments/createWithMedia.json">
    {
        id: "5225c8e51a88004a2f8fafa8",
        postId: "52204fab1a887135cbd8b484",
        createdAt: "Tue Sep 03 19:32:53 +0800 2013",
        text: "216秒语音评论",
        files: [ ],
        audio: {
            length: 346470,
            contentType: "audio/amr",
            contentUrl: "http://file.dev.lebooo.com/comment/5225c8e51a88004a2f8fafa8-audio-346470.amr",
            duration: 216
        },
        hasVideo: false,
        user: {
            id: "5211e1821a88ec201fd94f26",
            screenName: "明丫丫是个爷们",
            profileImageUrl: "http://file.dev.lebooo.com/5211e1831a88ec201fd94f27",
            profileImageBiggerUrl: "http://file.dev.lebooo.com/5211e1841a88ec201fd94f28",
            profileImageOriginalUrl: "http://file.dev.lebooo.com/5211e1841a88ec201fd94f29",
            createdAt: "Mon Aug 19 17:12:34 +0800 2013",
            followersCount: 0,
            friendsCount: 0,
            statusesCount: 5,
            favoritesCount: 0,
            beFavoritedCount: 0,
            viewCount: 0,
            digestCount: 0,
            weiboVerified: false,
            blocking: false,
            level: 0
        }
    }
</tags:example>