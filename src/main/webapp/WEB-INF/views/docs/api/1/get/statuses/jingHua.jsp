<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="精华" method="GET" action="${ctx}/api/1/statuses/jingHua.json">
    <tags:field name="count" optional="true"/>
    <tags:field name="maxId" optional="true"/>
    <tags:field name="sinceId" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/statuses/jingHua.json?count=1">
    [
        {
            id: "51f0c8671a8824c465355d59",
            user: {
                id: "51def1e61a883914869e46f3",
                screenName: "法图_麦",
                description: null,
                profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
                createdAt: "Fri Jul 12 01:56:54 +0800 2013",
                following: null,
                followersCount: 0,
                friendsCount: 1,
                statusesCount: 8,
                favoritesCount: 3,
                beFavoritedCount: 1,
                viewsCount: null
            },
            createdAt: "Thu Jul 25 14:40:39 +0800 2013",
            text: "@Desi_漓沫沫@小萌君sang @家有笨猫咪",
            files: [
                {
                    filename: "2013-07-10_12-04-58.mp4",
                    length: 883540,
                    contentType: "video/mp4",
                    contentUrl: "/files/51f0c8671a8824c465355d52?postId=51f0c8671a8824c465355d59"
                },
                {
                    filename: "2013-07-10_12-04-58.jpg",
                    length: 15874,
                    contentType: "image/jpeg",
                    contentUrl: "/files/51f0c8671a8824c465355d57?postId=51f0c8671a8824c465355d59"
                }
            ],
            source: "网页版",
            favorited: false,
            favoritesCount: 0,
            repostsCount: 0,
            reposted: false,
            commentsCount: 0,
            viewsCount: 0,
            originStatus: null,
            comments: [ ],
            userMentions: [
                "51def53f1a883914869e46f5",
                "51def52f1a883914869e46f4",
                "51def1ce1a883914869e46f2"
            ]
        }
    ]
</tags:example>