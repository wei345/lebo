<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<h2>设置</h2>

<ul>
    <li><a href="${ctx}/admin/settings">基本设置</a></li>
    <li><a href="${ctx}/admin/channels">频道设置</a></li>
    <li><a href="${ctx}/admin/recommendedApps">推荐应用</a></li>
</ul>

<h2>管理</h2>

<ul>
    <li><a href="${ctx}/admin/user">用户管理</a></li>
    <li><a href="${ctx}/admin/post/list">帖子管理</a></li>
    <li><a href="${ctx}/admin/comment/list">评论管理</a></li>
</ul>

<h2>发布</h2>

<ul>
    <li><a href="${ctx}/admin/task/publish-video">发布视频</a></li>
    <li><a href="${ctx}/admin/task/apns-all-user">推送通知</a></li>
</ul>