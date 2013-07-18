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
            "id": "51e6191da0eedbd1aad37b72",
            "userId": "51e6124ea0eedbd1aad37b6b",
            "createdAt": 1374034205925,
            "text": "中文abc",
            "truncated": false,
            "files": [],
            "source": null,
            "geoLocation": null,
            "postId": "51e6136ea0eedbd1aad37b71",
            "mentions": []
        }
    ]
</tags:example>