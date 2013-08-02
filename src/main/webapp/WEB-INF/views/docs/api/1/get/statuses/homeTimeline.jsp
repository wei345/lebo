<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="主页时间线" method="GET" action="${ctx}/api/1/statuses/homeTimeline.json">
    <tags:field name="count" optional="true"/>
    <tags:field name="maxId" optional="true"/>
    <tags:field name="sinceId" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/statuses/homeTimeline.json">
    [
        {
            id: "51ef48a01a88354de8dfb0c8",
            user: {
                id: "51def1e61a883914869e46f3",
                screenName: "法图_麦",
                profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
                createdAt: "Fri Jul 12 01:56:54 +0800 2013",
                following: null,
                followersCount: 0,
                friendsCount: 1,
                statusesCount: 6,
                favoritesCount: 2,
                beFavoritedCount: null,
                viewsCount: null
            },
            createdAt: "Wed Jul 24 11:23:12 +0800 2013",
            text: null,
            files: [ ],
            source: null,
            favorited: null,
            favoritesCount: null,
            repostsCount: null,
            reposted: null,
            commentsCount: null,
            viewsCount: 0,
            originStatus: {
                id: "51ee23411a88eaa41b348b72",
                user: {
                    id: "51e778ea1a8816dc79e40aaf",
                    screenName: "liuwei",
                    profileImageUrl: "/files/51ed11161a88f15acf2d87fd",
                    createdAt: "Thu Jul 18 13:11:06 +0800 2013",
                    following: false,
                    followersCount: 0,
                    friendsCount: 0,
                    statusesCount: 5,
                    favoritesCount: 1,
                    beFavoritedCount: 0,
                    viewsCount: 3
                },
                createdAt: "Tue Jul 23 14:31:28 +0800 2013",
                text: "杰克逊2",
                files: [
                    {
                        filename: "2013-07-01_17-54-29.mp4",
                        length: 431228,
                        contentType: "video/mp4",
                        contentUrl: "/files/51df969c1a88cb49eec1f5f2?postId=51ee23411a88eaa41b348b72"
                    },
                    {
                        filename: "2013-07-01_17-54-29.jpg",
                        length: 16021,
                        contentType: "image/jpeg",
                        contentUrl: "/files/51df969c1a88cb49eec1f5f5?postId=51ee23411a88eaa41b348b72"
                    }
                ],
                source: "网页版",
                favorited: false,
                favoritesCount: 0,
                repostsCount: 1,
                reposted: true,
                commentsCount: 0,
                viewsCount: 0,
                originStatus: null
            }
        }
    ]
</tags:example>