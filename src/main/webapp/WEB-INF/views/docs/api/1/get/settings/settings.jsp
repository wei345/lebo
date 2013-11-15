<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="应用设置" method="GET" action="${ctx}/api/1/settings.json">
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/settings.json">
    {
        officialAccountId: "5211e1821a88ec201fd94f26",
        digestAccountId: "525124e81a88ac9dfcbd9ce0",
        imPollingIntervalSeconds: 120
    }
</tags:example>