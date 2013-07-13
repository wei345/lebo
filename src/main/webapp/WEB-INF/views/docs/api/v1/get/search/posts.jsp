<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="搜索Posts" method="GET" action="${ctx}/api/v1/search/posts.json">
    <tags:field name="q" value="转发"/>
    <tags:field name="count" optional="true"/>
    <tags:field name="maxId" optional="true"/>
    <tags:field name="sinceId" optional="true"/>
</tags:form>


<tags:example method="GET" url="http://localhost:8080/api/v1/search/posts.json?q=%E8%BD%AC%E5%8F%91">
    [
        {
            id: "51dfddc31a88464ee138902d",
            userId: "51dfd3d21a8855744379891f",
            createdAt: 1373625795104,
            text: "转发2",
            truncated: false,
            files: [ ],
            source: null,
            geoLocation: null,
            originPostId: "51df9f8d1a8899de19ebe351",
            mentions: [ ],
            tags: [ ],
            repostsCount: 1
        },
        {
            id: "51dfd3ed1a88557443798920",
            userId: "51dfd3d21a8855744379891f",
            createdAt: 1373623277502,
            text: "转发1",
            truncated: false,
            files: [ ],
            source: null,
            geoLocation: null,
            originPostId: "51df9f8d1a8899de19ebe351",
            mentions: [ ],
            tags: [ ],
            repostsCount: null
        }
    ]
</tags:example>