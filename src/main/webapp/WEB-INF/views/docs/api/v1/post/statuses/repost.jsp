<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="转发视频" method="POST" action="${ctx}/api/v1/statuses/repost.json">
    <p>
        原视频：originStatus
    </p>
    <tags:field name="id" value=""/>
    <tags:field name="text" value="转发"/>
</tags:form>

<tags:example method="POST" url="">
    {
        id: "51dfd3ed1a88557443798920",
        user: {
            id: "51dfd3d21a8855744379891f",
            screenName: "xueeR_Z",
            name: "xueeR_Z",
            profileImageUrl: "http://tp4.sinaimg.cn/2484091107/50/5668404603/0",
            createdAt: 1373623250172,
            following: null,
            followersCount: 0,
            friendsCount: 0,
            statusesCount: 1,
            verified: null,
            location: null,
            timeZone: null
        },
        createdAt: 1373623277502,
        text: "转发1",
        truncated: false,
        files: [ ],
        source: null,
        geoLocation: null,
        favorited: false,
        favouritesCount: 0,
        repostsCount: null,
        reposted: null,
        commentsCount: null,
        originStatus: {
            id: "51df9f8d1a8899de19ebe351",
            user: {
                id: "51def1e61a883914869e46f3",
                screenName: "法图_麦",
                name: "法图_麦",
                profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
                createdAt: 1373565414511,
                following: false,
                followersCount: 0,
                friendsCount: 1,
                statusesCount: 1,
                verified: null,
                location: null,
                timeZone: null
            },
            createdAt: 1373609869117,
            text: "视频2",
            truncated: false,
            files: [
                "51df9f8d1a8899de19ebe34b",
                "51df9f8d1a8899de19ebe34f"
            ],
            source: null,
            geoLocation: null,
            favorited: false,
            favouritesCount: 1,
            repostsCount: null,
            reposted: null,
            commentsCount: null,
            originStatus: null
        }
    }
</tags:example>
