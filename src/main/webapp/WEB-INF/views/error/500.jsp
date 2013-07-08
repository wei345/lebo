<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.lebo.rest.ErrorDto" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%response.setStatus(200);%>
<%
    Throwable ex = null;
    if (exception != null)
        ex = exception;
    if (request.getAttribute("javax.servlet.error.exception") != null)
        ex = (Throwable) request.getAttribute("javax.servlet.error.exception");

    //记录日志
    Logger logger = LoggerFactory.getLogger("500.jsp");
    logger.error(ex.getMessage(), ex);
%>
<%
    String uri = (String) request.getAttribute("javax.servlet.forward.request_uri");
    if (uri != null && (uri.endsWith(".json") || uri.startsWith("/api"))) {
        response.getWriter().write(ErrorDto.newInternalServerError(ex.getMessage()).toJson());
    } else {
%>

<!DOCTYPE html>
<html>
<head>
    <title>500 - 系统内部错误</title>
</head>

<body>
<h2>500 - 系统发生内部错误.</h2>

<p><a href="<c:url value="/"/>">返回首页</a></p>
</body>
</html>
<% } %>
