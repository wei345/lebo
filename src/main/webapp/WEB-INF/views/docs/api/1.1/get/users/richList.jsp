<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="富豪排行榜" method="GET" action="${ctx}/api/1.1/users/richList.json">
    <tags:fields-page-size/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1.1/users/richList.json">

</tags:example>