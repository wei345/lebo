<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.lebo.rest.dto.ErrorDto" %>
<%response.setStatus(200);%><%
    String uri = (String) request.getAttribute("javax.servlet.forward.request_uri");
    if (uri != null && (uri.endsWith(".json") || uri.startsWith("/api"))) {
        response.getWriter().write(com.lebo.rest.dto.ErrorDto.BAD_REQUEST.toJson());
    } else { %>

<!DOCTYPE html>
<html>
<head>
    <title>400 - 错误的请求</title>
</head>

<body>
<h2>400 - 错误的请求.</h2>

<p><a href="<c:url value="/"/>">返回首页</a></p>
</body>
</html>
<% } %>