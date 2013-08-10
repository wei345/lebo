<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="标记全部未读通知为已读" method="POST" action="${ctx}/api/1/notifications/markAllRead.json">
</tags:form>

响应状态码：200，响应内容：无