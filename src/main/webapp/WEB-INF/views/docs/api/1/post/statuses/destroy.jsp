<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="删除视频" method="POST" action="${ctx}/api/1/statuses/destroy.json">
    <p>
        删除ID参数指定的Post。当前登录用户必须是Post的作者。如果成功返回被删除的Post。
    </p>
    <tags:field name="id" value=""/>
</tags:form>

<tags:example method="POST" url="">
    {
        id: "51f8cb271a88d0e664d18d3d",
        user: {
            id: "51dfd3d21a8855744379891f",
            screenName: "xueeR_Z",
            profileImageUrl: "http://tp4.sinaimg.cn/2484091107/50/5668404603/0",
            createdAt: "Fri Jul 12 18:00:50 +0800 2013",
            followersCount: 1,
            friendsCount: 2,
            statusesCount: 5,
            favoritesCount: 2,
            beFavoritedCount: 2,
            blocking: false
        },
        createdAt: "Wed Jul 31 16:30:31 +0800 2013",
        text: "视频#丐帮##少林#",
        files: [
            {
                filename: "2013-07-10_12-04-58.mp4",
                length: 883540,
                contentType: "video/mp4",
                contentUrl: "/files/51f0c8671a8824c465355d52?postId=51f8cb271a88d0e664d18d3d"
            },
            {
                filename: "2013-07-10_12-04-58.jpg",
                length: 15874,
                contentType: "image/jpeg",
                contentUrl: "/files/51f0c8671a8824c465355d57?postId=51f8cb271a88d0e664d18d3d"
            }
        ],
        source: "网页版",
        favorited: false,
        favoritesCount: 0,
        repostsCount: 0,
        reposted: false,
        commentsCount: 0,
        viewsCount: 0,
        comments: [ ],
        userMentions: [ ],
        digested: false
    }
</tags:example>
