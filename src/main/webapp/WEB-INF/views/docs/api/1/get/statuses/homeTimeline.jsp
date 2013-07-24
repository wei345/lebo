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
        id: "51ee266f1a88d7a3c9fb42dd",
        user: {
            id: "51e778ea1a8816dc79e40aaf",
            screenName: "liuwei",
            name: "Liu Wei",
            profileImageUrl: "/files/51ed11161a88f15acf2d87fd",
            createdAt: "Thu Jul 18 13:11:06 +0800 2013",
            following: null,
            followersCount: null,
            friendsCount: 0,
            statusesCount: 5,
            verified: null,
            location: null,
            timeZone: null,
            beFavoritedCount: 0,
            viewsCount: null
        },
        createdAt: "Tue Jul 23 14:45:03 +0800 2013",
        text: "杰克逊5",
        files: [
            {
                filename: "2013-07-01_17-54-29.mp4",
                length: 431228,
                contentType: "video/mp4",
                contentUrl: "/files/51df969c1a88cb49eec1f5f2?postId=51ee266f1a88d7a3c9fb42dd"
            },
            {
                filename: "2013-07-01_17-54-29.jpg",
                length: 16021,
                contentType: "image/jpeg",
                contentUrl: "/files/51df969c1a88cb49eec1f5f5?postId=51ee266f1a88d7a3c9fb42dd"
            }
        ],
        source: "网页版",
        favorited: false,
        favouritesCount: 0,
        repostsCount: 0,
        reposted: false,
        commentsCount: 0,
        viewsCount: null,
        originStatus: null
    },
    {
        id: "51ee24271a880b8ad8799b09",
        user: {
            id: "51e778ea1a8816dc79e40aaf",
            screenName: "liuwei",
            name: "Liu Wei",
            profileImageUrl: "/files/51ed11161a88f15acf2d87fd",
            createdAt: "Thu Jul 18 13:11:06 +0800 2013",
            following: null,
            followersCount: null,
            friendsCount: 0,
            statusesCount: 5,
            verified: null,
            location: null,
            timeZone: null,
            beFavoritedCount: 0,
            viewsCount: null
        },
        createdAt: "Tue Jul 23 14:35:18 +0800 2013",
        text: "杰克逊3",
        files: [
            {
                filename: "2013-07-01_17-54-29.mp4",
                length: 431228,
                contentType: "video/mp4",
                contentUrl: "/files/51df969c1a88cb49eec1f5f2?postId=51ee24271a880b8ad8799b09"
            },
            {
                filename: "2013-07-01_17-54-29.jpg",
                length: 16021,
                contentType: "image/jpeg",
                contentUrl: "/files/51df969c1a88cb49eec1f5f5?postId=51ee24271a880b8ad8799b09"
            }
        ],
        source: "网页版",
        favorited: false,
        favouritesCount: 0,
        repostsCount: 0,
        reposted: false,
        commentsCount: 0,
        viewsCount: 3,
        originStatus: null
    },
    {
        id: "51ee23411a88eaa41b348b72",
        user: {
            id: "51e778ea1a8816dc79e40aaf",
            screenName: "liuwei",
            name: "Liu Wei",
            profileImageUrl: "/files/51ed11161a88f15acf2d87fd",
            createdAt: "Thu Jul 18 13:11:06 +0800 2013",
            following: null,
            followersCount: null,
            friendsCount: 0,
            statusesCount: 5,
            verified: null,
            location: null,
            timeZone: null,
            beFavoritedCount: 0,
            viewsCount: null
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
        favouritesCount: 0,
        repostsCount: 0,
        reposted: false,
        commentsCount: 0,
        viewsCount: null,
        originStatus: null
    },
    {
        id: "51ee22a01a88ab951db570c6",
        user: {
            id: "51e778ea1a8816dc79e40aaf",
            screenName: "liuwei",
            name: "Liu Wei",
            profileImageUrl: "/files/51ed11161a88f15acf2d87fd",
            createdAt: "Thu Jul 18 13:11:06 +0800 2013",
            following: null,
            followersCount: null,
            friendsCount: 0,
            statusesCount: 5,
            verified: null,
            location: null,
            timeZone: null,
            beFavoritedCount: 0,
            viewsCount: null
        },
        createdAt: "Tue Jul 23 14:28:47 +0800 2013",
        text: "杰克逊1",
        files: [
            {
                filename: "2013-07-01_17-54-29.mp4",
                length: 431228,
                contentType: "video/mp4",
                contentUrl: "/files/51df969c1a88cb49eec1f5f2?postId=51ee22a01a88ab951db570c6"
            },
            {
                filename: "2013-07-01_17-54-29.jpg",
                length: 16021,
                contentType: "image/jpeg",
                contentUrl: "/files/51df969c1a88cb49eec1f5f5?postId=51ee22a01a88ab951db570c6"
            }
        ],
        source: "网页版",
        favorited: false,
        favouritesCount: 0,
        repostsCount: 0,
        reposted: false,
        commentsCount: 0,
        viewsCount: null,
        originStatus: null
    },
    {
        id: "51e8be921a88baf326bf010a",
        user: {
            id: "51e778ea1a8816dc79e40aaf",
            screenName: "liuwei",
            name: "Liu Wei",
            profileImageUrl: "/files/51ed11161a88f15acf2d87fd",
            createdAt: "Thu Jul 18 13:11:06 +0800 2013",
            following: null,
            followersCount: null,
            friendsCount: 0,
            statusesCount: 5,
            verified: null,
            location: null,
            timeZone: null,
            beFavoritedCount: 0,
            viewsCount: null
        },
        createdAt: "Fri Jul 19 12:20:32 +0800 2013",
        text: "转发123",
        files: [
            {
                filename: "2013-07-10_12-26-26.mp4",
                length: 997271,
                contentType: "video/mp4",
                contentUrl: "/files/51e3a0c91a8890916e962c8d?postId=51e3a0ca1a8890916e962c94"
            },
            {
                filename: "2013-07-10_12-26-26.jpg",
                length: 26038,
                contentType: "image/jpeg",
                contentUrl: "/files/51e3a0c91a8890916e962c92?postId=51e3a0ca1a8890916e962c94"
            }
        ],
        source: null,
        favorited: false,
        favouritesCount: 0,
        repostsCount: 1,
        reposted: true,
        commentsCount: 0,
        viewsCount: null,
        originStatus: {
            id: "51e3a0ca1a8890916e962c94",
            user: {
                id: "51def1e61a883914869e46f3",
                screenName: "法图_麦",
                name: "法图_麦",
                profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
                createdAt: "Fri Jul 12 01:56:54 +0800 2013",
                following: false,
                followersCount: null,
                friendsCount: 1,
                statusesCount: 5,
                verified: null,
                location: null,
                timeZone: null,
                beFavoritedCount: null,
                viewsCount: null
            },
            createdAt: "Mon Jul 15 15:12:09 +0800 2013",
            text: "#searchTerms#@显示名 今天下雨了，@虚竹@无名#华山论剑#@杨过",
            files: [
                {
                    filename: "2013-07-10_12-26-26.mp4",
                    length: 997271,
                    contentType: "video/mp4",
                    contentUrl: "/files/51e3a0c91a8890916e962c8d?postId=51e3a0ca1a8890916e962c94"
                },
                {
                    filename: "2013-07-10_12-26-26.jpg",
                    length: 26038,
                    contentType: "image/jpeg",
                    contentUrl: "/files/51e3a0c91a8890916e962c92?postId=51e3a0ca1a8890916e962c94"
                }
            ],
            source: "乐播网页版",
            favorited: false,
            favouritesCount: 0,
            repostsCount: 1,
            reposted: true,
            commentsCount: 0,
            viewsCount: null,
            originStatus: null
        }
    }
]
</tags:example>