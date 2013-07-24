<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="取消收藏" method="POST" action="${ctx}/api/1/favorites/destroy.json">
    <tags:field name="postId" value="51df969c1a88cb49eec1f5f7"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1/favorites/destroy.json?postId=51e6136ea0eedbd1aad37b71">
    {
        id: "51df969c1a88cb49eec1f5f7",
        user: {
            id: "51def53f1a883914869e46f5",
            screenName: "家有笨猫咪",
            name: "家有笨猫咪",
            profileImageUrl: "http://tp3.sinaimg.cn/3472643302/50/5663730129/0",
            createdAt: "Fri Jul 12 02:11:11 +0800 2013",
            following: false,
            followersCount: 10,
            friendsCount: 0,
            statusesCount: 1,
            verified: null,
            location: null,
            timeZone: null,
            beFavoritedCount: null,
            viewsCount: null
        },
        createdAt: "Fri Jul 12 13:39:40 +0800 2013",
        text: "视频1",
        truncated: false,
        files: [
            {
                filename: "2013-07-01_17-54-29.mp4",
                length: 431228,
                contentType: "video/mp4",
                contentUrl: "/files/51df969c1a88cb49eec1f5f2?postId=51df969c1a88cb49eec1f5f7"
            },
            {
                filename: "2013-07-01_17-54-29.jpg",
                length: 16021,
                contentType: "image/jpeg",
                contentUrl: "/files/51df969c1a88cb49eec1f5f5?postId=51df969c1a88cb49eec1f5f7"
            }
        ],
        source: null,
        geoLocation: null,
        favorited: false,
        favouritesCount: 1,
        repostsCount: 0,
        reposted: false,
        commentsCount: 0,
        originStatus: null
    }
</tags:example>