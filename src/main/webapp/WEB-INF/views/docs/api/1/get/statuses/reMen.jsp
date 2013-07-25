<%@ page import="com.lebo.entity.Post" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="热门" method="GET" action="${ctx}/api/1/statuses/reMen.json">
    每页大小5-200
    <tags:field name="size" value="5" optional="true"/>
    第几页，从0开始，0返回第1页数据
    <tags:field name="page" value="2" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/statuses/reMen.json?size=2">
    [
        {
            id: "51ef6f741a8847c724a187f7",
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
            createdAt: "Wed Jul 24 14:08:51 +0800 2013",
            text: "视频",
            files: [
                {
                    filename: "2013-07-03_14-54-43.mp4",
                    length: 905357,
                    contentType: "video/mp4",
                    contentUrl: "/files/51e0f3a01a88d25e8fec368e?postId=51ef6f741a8847c724a187f7"
                },
                {
                    filename: "2013-07-03_14-54-43.jpg",
                    length: 4227,
                    contentType: "image/jpeg",
                    contentUrl: "/files/51e0f3a01a88d25e8fec3693?postId=51ef6f741a8847c724a187f7"
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
            userMentions: [ ]
        },
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