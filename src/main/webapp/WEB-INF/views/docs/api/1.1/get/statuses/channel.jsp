<%@ page import="com.lebo.entity.Post" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="lastCommentCreatedAt" value="<%=Post.LAST_COMMENT_CREATED_AT_KEY%>"/>
<c:set var="createdAt" value="<%=Post.CREATED_AT_KEY%>"/>
<c:set var="favoritesCount" value="<%=Post.FAVOURITES_COUNT_KEY%>"/>

<tags:form name="频道视频 v1.1" action="${ctx}/api/1.1/statuses/channel.json" method="GET">
    <p>
        频道名
    </p>
    <tags:field name="name" value="最新视频"/>

    <p>
        排序字段：<code>${lastCommentCreatedAt}</code>, <code>${createdAt}</code>, <code>${favoritesCount}</code>
    </p>
    <tags:field name="orderBy"/>

    <tags:fields-page-size/>

</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1.1/statuses/channel.json?name=%E6%9C%80%E6%96%B0%E8%A7%86%E9%A2%91&orderBy=createdAt&size=2">
    [
        {
            id: "52a542e11a8841b0c325b1b0",
            user: {
                id: "525124e81a88ac9dfcbd9ce0",
                screenName: "明丫丫是个爷们",
                profileImageUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce1",
                profileImageBiggerUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce3",
                profileImageOriginalUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce5",
                createdAt: "Sun Oct 06 16:52:56 +0800 2013",
                following: false,
                followersCount: 0,
                friendsCount: 0,
                statusesCount: 12,
                originPostsCount: 12,
                repostsCount: 0,
                beFavoritedCount: 1,
                viewCount: 2,
                digestCount: 0,
                weiboVerified: false,
                bilateral: false,
                level: 0
            },
            createdAt: "Mon Dec 09 12:11:13 +0800 2013",
            text: "测试定时发布视频",
            video: {
                length: 713985,
                contentType: "video/mp4",
                contentUrl: "http://file.dev.lebooo.com/post/2013-12-09/52a542e11a8841b0c325b1b0-video-713985.mp4",
                duration: 7
            },
            videoFirstFrameUrl: "http://file.dev.lebooo.com/post/2013-12-09/52a542e11a8841b0c325b1b0-video-first-frame-38533.jpg",
            source: "后台",
            favorited: false,
            favoritesCount: 0,
            repostsCount: 1,
            reposted: true,
            commentsCount: 1,
            viewCount: 0,
            shareCount: 1,
            comments: [
                {
                    id: "52a6dbe81a88ba7d4557af80",
                    postId: "52a542e11a8841b0c325b1b0",
                    createdAt: "Tue Dec 10 17:16:24 +0800 2013",
                    text: "测试更新帖子评论数",
                    hasVideo: false,
                    user: {
                        id: "52356929343539a89a52dc8d",
                        screenName: "admin",
                        description: "秋",
                        profileImageUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-normal-4252.png",
                        profileImageBiggerUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-bigger-16865.png",
                        profileImageOriginalUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-original-1705885.png",
                        profileBackgroundImageUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-background-image-1009721.png",
                        followersCount: 11,
                        friendsCount: 0,
                        statusesCount: 13,
                        originPostsCount: 7,
                        repostsCount: 6,
                        beFavoritedCount: 0,
                        viewCount: 2,
                        digestCount: 0,
                        level: 0
                    }
                }
            ],
            userMentions: [ ],
            digest: false,
            pvt: false
        }
    ]
</tags:example>