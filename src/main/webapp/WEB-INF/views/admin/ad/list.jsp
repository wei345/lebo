<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>广告管理</title>
    <style type="text/css">
        #contentTable th, #contentTable td {
            text-align: center;
        }

        #contentTable .detail {
            padding: 10px;
            text-align: left;
        }

        #contentTable td{
            vertical-align: middle;
        }

        #contentTable .preview{
            width: 100px;
            height: 100px;
        }

        #contentTable ul{
            list-style: none;
        }

        #contentTable .reallyOrder{
            width: 2em;
        }
    </style>
</head>
<body>
<label for="display-enabled-only" class="pull-left" onclick="toggleEnabledOnly()"><input type="checkbox"
                                                                                         id="display-enabled-only">只显示已启用的</label>

<a href="${ctx}/admin/ads/create" class="pull-right">+ 新建</a>

<table id="contentTable" class="table table-bordered table-condensed table-hover">
    <thead>
    <tr>
        <th class="reallyOrder">#</th>
        <th>预览</th>
        <th>详细</th>
        <th class="input-mini">顺序</th>
        <th class="input-medium">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${ads}" var="ad" varStatus="varStatus">
        <tr class="${ad.enabled == true? "enabled" : ""}">
            <td class="reallyOrder">
            </td>

            <td class="preview">
                <div>
                    <a href="${ad.url}" title="${ad.subject}">
                        <img class="input-small" src="${ad.imageUrl}"/>
                    </a>
                </div>
            </td>

            <td class="detail">
                <div>主题：${ad.subject}</div>
                <div>描述：${ad.description}</div>
                <div>跳转：${ad.url}</div>
                <div>组：${ad.group}</div>
            </td>
            <td class="channel-order">${ad.order}</td>
            <td class="actions">
                <div class="btn-group">
                    <button class="btn"
                            onclick="updateStatus('${ad.id}', this)">${ad.enabled ? "禁用" : "启用"}</button>
                    <button class="btn" onclick="location.href='${ctx}/admin/ads/update/${ad.id}'">编辑</button>
                    <button class="btn"
                            onclick="if(confirm('删除 ${ad.subject} ?')) $('#deleteForm').attr('action', '${ctx}/admin/ads/delete/${ad.id}').submit()">
                        删除
                    </button>
                </div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<form id="deleteForm" method="post" action="" style="display: none"></form>

<script>
    function updateReallyOrder() {
        var num = 1;
        $('#contentTable tr').each(function () {
            if ($(this).hasClass('enabled')) {
                $('td:first', this).html(num++);
            } else {
                $('td:first', this).html('');
            }
        });
    }

    function toggleEnabledOnly() {
        //隐藏已禁用的频道
        if ($('input#display-enabled-only')[0].checked) {
            $('#contentTable tr:gt(0)').each(function () {
                if (!$(this).hasClass('enabled')) {
                    $(this).hide();
                }
            });
        }
        //显示全部频道
        else {
            $('#contentTable tr').show();
        }
    }

    function updateStatus(id, btn) {
        var action = $(btn).html() == '启用' ? 'enable' : 'disable';

        $.ajax({
            url: '${ctx}/admin/ads/' + action + '/' + id,
            type: 'post',
            success: function (data) {
                if (data == 'ok') {
                    if (action == 'enable') {
                        $(btn).parents('tr').addClass('enabled');
                        $(btn).html('禁用');
                    } else {
                        $(btn).parents('tr').removeClass('enabled');
                        $(btn).html('启用');
                    }
                    updateReallyOrder();
                }
            }
        });
    }
    //onload
    updateReallyOrder();
</script>

</body>
</html>



