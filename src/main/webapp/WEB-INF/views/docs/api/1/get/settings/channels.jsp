<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<p>
    频道列表
</p>
<tags:form name="选项" method="GET" action="${ctx}/api/1/settings/channels.json">
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/settings/channels.json">
    [
        {
            name: "运动",
            contentUrl: null,
            image: "51e8af331a8835c0f2160bc5",
            backgroundColor: "#123456",
            enabled: false
        }
    ]
</tags:example>