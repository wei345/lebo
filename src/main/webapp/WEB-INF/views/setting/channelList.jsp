<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<a href="${ctx}/admin/channels/create" class="pull-right">+ 新建频道</a>

<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
    <thead>
    <tr>
        <th class="input-small">频道名</th>
        <th class="input-small">显示名</th>
        <th class="input-large">描述</th>
        <th class="input-small">图片</th>
        <th class="input-mini">背景颜色</th>
        <th class="input-small">follow</th>
        <th class="input-small">track</th>
        <th class="input-small">顺序</th>
        <th class="input-mini">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${channels}" var="channel">
        <tr>
            <td>${channel.name}</td>
            <td>${channel.title}</td>
            <td>${channel.description}</td>
            <td style="background-color: ${channel.backgroundColor}"><img class="input-small"
                                                                          src="${channel.imageUrl}"/></td>
            <td><span
                    style="background-color: ${channel.backgroundColor}; display: inline-block; width: 1em;">&nbsp;</span>${channel.backgroundColor}
            </td>
            <td>${channel.follow}</td>
            <td>${channel.track}</td>
            <td>${channel.order}</td>
            <td>
                <a class="btn" href="${ctx}/admin/channels/update/${channel.id}">编辑</a>

                <form method="post" action="${ctx}/admin/channels/delete/${channel.id}">
                    <button class="btn" type="submit">删除</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

