<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="提到我时间线" method="GET" action="${ctx}/api/1/statuses/mentionsTimeline.json">
    <tags:field name="count" optional="true"/>
    <tags:field name="maxId" optional="true"/>
    <tags:field name="sinceId" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/statuses/mentionsTimeline.json">
    [
        {
            id: "51deb74d1a883ab010ebcc2e",
            user: {
                id: "51dcf1d81a883e712783f124",
                screenName: "法图_麦",
                name: "法图_麦",
                description: null,
                profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
                createdAt: "2013-07-10 13:32:08",
                verified: null,
                location: null,
                timeZone: null
            },
            createdAt: 1373550413109,
            text: "法图_麦提到了@小萌君sang 完毕",
            truncated: false,
            files: [
                "51dcf33c1a883e712783f14f",
                "51dcf33c1a883e712783f153"
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