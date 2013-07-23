<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="查看黑名单" action="${ctx}/api/1/blocks/list.json" method="GET">
    <tags:field name="page" value="0" optional="true"/>
    <tags:field name="size" value="20" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/blocks/list.json">

</tags:example>