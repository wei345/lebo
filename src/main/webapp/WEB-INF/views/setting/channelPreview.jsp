<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<c:if test="${channels == null}">
    <div class="alert alert-error controls">
        <button class="close" data-dismiss="alert">×</button>
        JSON格式错误
    </div>
</c:if>
<c:if test="${channels != null}">
    <table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
        <thead>
        <tr>
            <th>ID</th>
            <th>名称</th>
            <th>描述</th>
            <th>图片</th>
            <th>背景颜色</th>
            <th>follow</th>
            <th>track</th>
            <th>启用/禁用</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${channels}" var="channel">
            <tr>
                <td>${channel.id}</td>
                <td>${channel.name}</td>
                <td>${channel.description}</td>
                <td><img class="input-small" src="${ctx}/files/${channel.image}"/></td>
                <td><span
                        style="background-color: ${channel.backgroundColor}; display: inline-block; width: 1em;">&nbsp;</span>${channel.backgroundColor}
                </td>
                <td>${channel.follow}</td>
                <td>${channel.track}</td>
                <td>${channel.enabled ? "启用" : "禁用"}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>