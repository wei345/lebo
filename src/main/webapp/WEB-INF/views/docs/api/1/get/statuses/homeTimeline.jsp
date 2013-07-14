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
            id: "51de59a21a882be8bd6d9019",
            user: {
                id: "51dcf1f71a883e712783f12c",
                screenName: "小萌君sang",
                name: "小萌君sang",
                description: null,
                profileImageUrl: "http://tp1.sinaimg.cn/3473952784/50/5663727050/0",
                createdAt: "2013-07-10 13:32:39",
                verified: null,
                location: null,
                timeZone: null
            },
            createdAt: 1373526434863,
            text: "视频6",
            truncated: false,
            files: [
                "51de59a21a882be8bd6d9012",
                "51de59a21a882be8bd6d9017"
            ],
            source: null,
            geoLocation: null,
            favouritesCount: null,
            repostsCount: null,
            reposted: null,
            originPostId: null
        },
        {
            id: "51de1c3c1a88b82f949585b4",
            user: {
                id: "51dcf1f71a883e712783f12c",
                screenName: "小萌君sang",
                name: "小萌君sang",
                description: null,
                profileImageUrl: "http://tp1.sinaimg.cn/3473952784/50/5663727050/0",
                createdAt: "2013-07-10 13:32:39",
                verified: null,
                location: null,
                timeZone: null
            },
            createdAt: 1373510716011,
            text: "视频5",
            truncated: false,
            files: [
                "51de1c3b1a88b82f949585ad",
                "51de1c3c1a88b82f949585b2"
            ],
            source: null,
            geoLocation: null,
            favouritesCount: null,
            repostsCount: null,
            reposted: null,
            originPostId: null
        }
    ]
</tags:example>