<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="发布评论 v1.1" method="POST" action="${ctx}/api/1.1/comments/createWithMedia.json">
    <ul>
        <li>回复帖子：需要 postId (被回复的帖子id)</li>
        <li>回复评论：需要 replyCommentId (被回复的评论id) 和 postId</li>
    </ul>
    <tags:field name="postId" value="" optional="true"/>
    <tags:field name="replyCommentId" value="" optional="true"/>
    <ul>
        <li>视频评论：需要 videoUrl 和 imageUrl</li>
        <li>语音评论：需要 audioUrl</li>
    </ul>
    <tags:field name="videoUrl" optional="true"/>
    <tags:field name="imageUrl" optional="true"/>
    <tags:field name="audioUrl" optional="true"/>
    <p>
        语音时长，整数
    </p>
    <tags:field name="audioDuration" optional="true"/>
    <tags:textarea name="text" value="" optional="true"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1.1/comments/createWithMedia.json">
    {
        id: "52706c731a88ee87420528b9",
        postId: "52651bae1a880cd6b8ab1c35",
        createdAt: "Wed Oct 30 10:18:27 +0800 2013",
        audio: {
            length: 18758,
            contentType: "audio/amr",
            contentUrl: "http://file.dev.lebooo.com/comment/2013-10-30/52706c731a88ee87420528b9-audio-18758.amr",
            duration: 11
        },
        hasVideo: false,
        user: {
            id: "52356929343539a89a52dc8d",
            screenName: "admin",
            followersCount: 0,
            friendsCount: 0,
            statusesCount: 6,
            beFavoritedCount: 0,
            viewCount: 0,
            digestCount: 0,
            level: 0
        }
    }
</tags:example>