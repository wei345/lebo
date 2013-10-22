<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="发布视频" method="POST" action="${ctx}/api/1.1/statuses/update.json">
    <tags:field name="videoUrl"/>
    <tags:field name="imageUrl"/>
    <tags:textarea name="text" value="视频"/>
</tags:form>

<tags:example method="POST" url="">
    {
        id: "52651a241a88064aeb74ef29",
        user: {
            id: "52356929343539a89a52dc8d",
            screenName: "admin",
            followersCount: 0,
            friendsCount: 0,
            statusesCount: 3,
            beFavoritedCount: 0,
            viewCount: 0,
            digestCount: 0,
            level: 0
        },
        createdAt: "Mon Oct 21 20:12:20 +0800 2013",
        text: "客户端通过签名URL上传到OSS",
        video: {
            length: 621825,
            contentType: "video/mp4",
            contentUrl: "http://file.dev.lebooo.com/post/2013-10-21/52651a241a88064aeb74ef29-video-621825.mp4"
        },
        videoFirstFrameUrl: "http://file.dev.lebooo.com/post/2013-10-21/52651a241a88064aeb74ef29-video-first-frame-15874.jpg",
        favorited: false,
        favoritesCount: 0,
        repostsCount: 0,
        reposted: false,
        commentsCount: 0,
        viewCount: 0,
        comments: [ ],
        userMentions: [ ],
        digest: false
    }
</tags:example>
