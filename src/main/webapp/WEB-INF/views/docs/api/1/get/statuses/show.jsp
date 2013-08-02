<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="单个视频" action="${ctx}/api/1/statuses/show.json" method="GET">
    <p>
        返回由id参数指定的单个视频。
    </p>
    <tags:field name="id" value="51f8db8a1a88178f820c1422"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/statuses/show.json?id=51f8db8a1a88178f820c1422">
    {
        id: "51f8db8a1a88178f820c1422",
        user: {
            id: "51dfd3d21a8855744379891f",
            screenName: "xueeR_Z",
            profileImageUrl: "http://tp4.sinaimg.cn/2484091107/50/5668404603/0",
            createdAt: "Fri Jul 12 18:00:50 +0800 2013",
            following: true,
            followersCount: 1,
            friendsCount: 2,
            statusesCount: 3,
            favoritesCount: 2,
            beFavoritedCount: 2,
            blocking: false
        },
        createdAt: "Wed Jul 31 17:40:26 +0800 2013",
        text: "视频#番石榴##獴狐猴#",
        files: [
            {
                filename: "2013-07-10_12-11-16.mp4",
                length: 1043721,
                contentType: "video/mp4",
                contentUrl: "/files/51e29dfd1a8881a5c61992f7?postId=51f8db8a1a88178f820c1422"
            },
            {
                filename: "2013-07-10_12-11-16.jpg",
                length: 34869,
                contentType: "image/jpeg",
                contentUrl: "/files/51e29dfd1a8881a5c61992fc?postId=51f8db8a1a88178f820c1422"
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
