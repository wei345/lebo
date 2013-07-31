<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="查看评论" action="${ctx}/api/1/comments/show.json" method="GET">
    <p>
        返回评论列表，按时间降序，新的在前、旧的在后。
    </p>
    <tags:field name="postId"/>
    <p>
        true - 返回视频评论，false - 返回非视频评论，不传 - 返回视频评论和非视频评论
    </p>
    <tags:field name="hasVideo" optional="true"/>
    <tags:field name="count" optional="true"/>
    <tags:field name="maxId" optional="true"/>
    <tags:field name="sinceId" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/comments/show.json">
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
