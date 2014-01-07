<%@ page import="com.lebo.entity.RobotSaying" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.springframework.data.domain.Page" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>机器人语库</title>
</head>
<body>

<a href="${ctx}/admin/robot/saying/add" class="pull-right">+ 添加</a>

<tags:pageinfo page="${page}"/>

<table id="contentTable" class="table table-bordered table-condensed table-hover">
    <thead>
    <tr>
        <th style="width:15em;">内容</th>
        <th style="width:6em;">标签</th>
        <th style="width:3em;">操作</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (RobotSaying item : ((Page<RobotSaying>) request.getAttribute("page")).getContent()) {
            request.setAttribute("item", item);
    %>
    <tr>
        <td>${item.text}</td>
        <td><%=StringUtils.join(item.getTags(), ",")%>
        </td>
        <td>
            <a href="${ctx}/admin/robot/saying/update/${item.id}" target="_blank">修改</a>
            <button class="btn btn-link" onclick="deleteSaying('${item.id}', this)">删除</button>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

<tags:pagination-normal page="${page}" paginationSize="5"/>

<script>
    function deleteSaying(id, btn) {
        var text = $(btn).parents('tr').find('td:first').text();
        if(text.length > 23){
            text = text.substring(0, 10) + "..." + text.substring(text.length - 10, text.length);
        }

        if (!confirm('确定删除 "'+ text +'" 吗？')) {
            return;
        }

        $.ajax({
            url: '${ctx}/admin/robot/saying/delete/' + id,
            type: 'POST',
            success: function (data) {
                if (data == 'ok') {
                    $(btn).parents('tr').remove();
                } else {
                    deleteFail(data, btn);
                }
            },
            error: function (jqXHR) {
                deleteFail(jqXHR.responseText, btn);
            }
        });

    }

    function deleteFail(msg, btn) {
        $('<span class="alert-error"></span>').insertAfter(btn).html(msg);
    }
</script>
</body>
</html>