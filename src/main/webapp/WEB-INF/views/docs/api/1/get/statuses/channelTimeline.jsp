<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="频道posts" method="GET" action="${ctx}/api/1/statuses/channelTimeline.json">
    <p>
        返回由id参数指定频道的视频列表。
    </p>
    <tags:field name="name" value="最新视频"/>
    <tags:field name="count" optional="true"/>
    <tags:field name="maxId" optional="true"/>
    <tags:field name="sinceId" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/statuses/channelTimeline.json?name=who_is_next&count=2">
    [
        {
            id: "51f906781a88b127ccb0f42b",
            user: {
                id: "51f2285a1a88ffcbb01b7f43",
                screenName: "Lau-s1r",
                profileImageUrl: "http://tp2.sinaimg.cn/1788028385/50/5667934296/0",
                createdAt: "Fri Jul 26 15:42:18 +0800 2013",
                following: false,
                followersCount: 0,
                friendsCount: 0,
                statusesCount: 1,
                favoritesCount: 0,
                beFavoritedCount: 0,
                viewCount: 0,
                weiboVerified: false,
                blocking: false
            },
            createdAt: "Wed Jul 31 20:43:35 +0800 2013",
            text: "视频#金银花#",
            files: [
                {
                    filename: "2013-07-10_12-31-14.mp4",
                    length: 851618,
                    contentType: "video/mp4",
                    contentUrl: "/files/51e437701a88da1af1c21c6b?postId=51f906781a88b127ccb0f42b"
                },
                {
                    filename: "2013-07-10_12-31-14.jpg",
                    length: 33295,
                    contentType: "image/jpeg",
                    contentUrl: "/files/51e437701a88da1af1c21c70?postId=51f906781a88b127ccb0f42b"
                }
            ],
            source: "网页版",
            favorited: false,
            favoritesCount: 0,
            repostsCount: 0,
            reposted: false,
            commentsCount: 0,
            viewCount: 0,
            comments: [ ],
            userMentions: [ ],
            digest: false
        },
        {
            id: "51f8db8a1a88178f820c1422",
            user: {
                id: "51dfd3d21a8855744379891f",
                screenName: "xueeR_Z",
                profileImageUrl: "http://tp4.sinaimg.cn/2484091107/50/5668404603/0",
                createdAt: "Fri Jul 12 18:00:50 +0800 2013",
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
            viewCount: 0,
            comments: [ ],
            userMentions: [ ],
            digest: false
        }
    ]
</tags:example>

