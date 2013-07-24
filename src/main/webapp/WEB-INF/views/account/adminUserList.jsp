<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>用户管理</title>
    <script>
        $(document).ready(function() {
            //聚焦第一个输入框
            $("#q").focus();
            //为inputForm注册validate函数
            $("#searchForm").validate({
                rules: {
                    count: {
                        number: true,
                        min: 5,
                        max: 1000,
                        required: true
                    }
                }
            });
        });
    </script>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>

    <form id="searchForm" class="form-search pull-left" method="GET" action="">
        <input type="search" class="input-medium search-query" id="q" name="q" value="${q}" placeholder="正则表达式">
        <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;" onclick="$('input[name=q]').val('')"></span>
        orderBy <input type="text" class="input-mini" name="orderBy" value="${orderBy}" placeholder="followersCount/beFavoritedCount/viewsCount">
        <select class="input-small" name="order">
            <setting ${order == "desc" ? "selected='selected'" : ""} value="desc">desc</setting>
            <setting ${order == "asc" ? "selected='selected'" : ""} value="asc">asc</setting>
        </select>
        <input type="text" class="input-mini" name="size" value="${size}">条/页
        <button type="submit" class="btn" style="margin-left: 2em;">搜索</button>
    </form>

    <a href="${ctx}/register" class="pull-right">添加新用户</a>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
		<thead><tr><th>用户名</th><th>邮件地址</th><th>姓名</th><th>注册时间<th>管理</th></tr></thead>
		<tbody>
		<c:forEach items="${users}" var="user">
			<tr>
				<td><a href="${ctx}/admin/user/update/${user.id}">${user.screenName}</a></td>
				<td>${user.email}</td>
				<td>${user.name}</td>
				<td>
					<fmt:formatDate value="${user.createdAt}" pattern="yyyy年MM月dd日  HH时mm分ss秒" />
				</td>
				<td><a href="${ctx}/admin/user/delete/${user.id}">删除</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
    <c:if test="${empty users}">
        <div style="font-style: italic; text-align: center">没有了</div>
    </c:if>

    <tags:pagination page="${page}" size="${size}" currentSize="${currentSize}" />
</body>
</html>
