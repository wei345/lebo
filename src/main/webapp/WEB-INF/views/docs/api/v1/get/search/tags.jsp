<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="搜索Tags" method="GET" action="${ctx}/api/v1/search/tags.json">
    <tags:field name="q" value="标签"/>
    <tags:field name="count" optional="true"/>
    <tags:field name="maxId" optional="true"/>
    <tags:field name="sinceId" optional="true"/>
</tags:form>


<tags:example method="GET" url="http://localhost:8080/api/v1/search/tags.json?q=%E6%A0%87%E7%AD%BE">
    [
        {
            id: "51e0f3a01a88d25e8fec3695",
            user: {
                id: "51def1e61a883914869e46f3",
                screenName: "法图_麦",
                name: "法图_麦",
                profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
                createdAt: 1373565414511,
                following: null,
                followersCount: 0,
                friendsCount: 1,
                statusesCount: 2,
                verified: null,
                location: null,
                timeZone: null
            },
            createdAt: 1373696928214,
            text: "#标签1# #标签2#",
            truncated: false,
            files: [
                "51e0f3a01a88d25e8fec368e",
                "51e0f3a01a88d25e8fec3693"
            ],
            source: "乐播网页版",
            geoLocation: null,
            favorited: false,
            favouritesCount: 0,
            repostsCount: null,
            reposted: null,
            commentsCount: null,
            originStatus: null
        }
    ]
</tags:example>