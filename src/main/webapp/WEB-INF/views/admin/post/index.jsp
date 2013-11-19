<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>帖子管理</title>
</head>
<body>
<ul>
    <li><a href="${ctx}/admin/post/list">帖子列表</a></li>
    <li><a href="${ctx}/admin/post/hot">热门帖子</a></li>
</ul>
</body>
</html>