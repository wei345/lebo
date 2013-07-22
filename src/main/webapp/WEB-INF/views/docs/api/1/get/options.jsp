<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="选项" method="GET" action="${ctx}/api/1/options.json">
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/options.json">
    {
        channels: [
            {
                name: "运动",
                contentUrl: null,
                image: "51e8af331a8835c0f2160bc5",
                backgroundColor: "#123456",
                enabled: false
            }
        ],
        officialAccountId: "51def1e61a883914869e46f3",
        bestContentUrl: "/api/1/statuses/filter?follow=51def1e61a883914869e46f3&after=Sun+Jul+14+14%3A24%3A59+%2B0800+2013"
    }
</tags:example>