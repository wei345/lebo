<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="查看收藏" action="${ctx}/api/1/favorites/show.json" method="GET">
    <tags:field name="count" optional="true"/>
    <tags:field name="maxId" optional="true"/>
    <tags:field name="sinceId" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/favorites/show.json">
    [
        {
            "id": "51e6136ea0eedbd1aad37b71",
            "user": {
                "id": "51e6124ea0eedbd1aad37b6b",
                "screenName": "xueeR_Z",
                "name": "xueeR_Z",
                "profileImageUrl": "http://tp4.sinaimg.cn/2484091107/50/5668404603/0",
                "createdAt": 1374032462234,
                "following": null,
                "followersCount": 0,
                "friendsCount": 0,
                "statusesCount": 1,
                "verified": null,
                "location": null,
                "timeZone": null
            },
            "createdAt": 1374032749779,
            "text": "视频fkdjfdjfkj",
            "truncated": false,
            "files": ["51e6136da0eedbd1aad37b6c", "51e6136da0eedbd1aad37b6f"],
            "source": "网页版",
            "geoLocation": null,
            "favorited": true,
            "favouritesCount": 1,
            "repostsCount": null,
            "reposted": null,
            "commentsCount": null,
            "originStatus": null
        }
    ]
</tags:example>