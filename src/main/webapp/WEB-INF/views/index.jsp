<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Welcome</title>
</head>

<body>
<ul>
    <li><tags:link url="${ctx}/docs/api/1" text="REST API v1"/></li>
    <li><tags:link url="${ctx}/admin/user" text="后台"/></li>
</ul>
</body>




