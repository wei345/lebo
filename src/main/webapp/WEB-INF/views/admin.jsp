<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<ul>
    <li><a href="${ctx}/admin/user">用户管理</a></li>
    <li><a href="${ctx}/admin/channels">频道管理</a></li>
    <li><a href="${ctx}/admin/settings">设置</a></li>
</ul>