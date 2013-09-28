<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<ul>
    <li><a href="${ctx}/admin/settings">基本设置</a></li>
    <li><a href="${ctx}/admin/channels">频道管理</a></li>
    <li><a href="${ctx}/admin/user">用户管理</a></li>
    <li><a href="${ctx}/admin/task/publish-video">发布视频</a></li>
    <li><a href="${ctx}/admin/post/list">管理视频</a></li>
</ul>