<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<head>
    <title>Welcome</title>
</head>

<body>
<ul>
    <li><tags:link url="${ctx}/docs/api/1" text="REST API v1"/></li>
    <shiro:hasRole name="admin">
        <li><tags:link url="${ctx}/admin" text="后台"/></li>
    </shiro:hasRole>
</ul>
</body>




