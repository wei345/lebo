<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>频道设置</title>
    <style type="text/css">
        #contentTable th {
            text-align: center;
        }

        td.channel-image > div {
            width: 100px;
            height: 100px;
            overflow: hidden;
            margin: 10px;
            position: relative;
            text-align: center;
        }

        td.channel-image .display-name {
            position: absolute;
            bottom: 3px;
            width: 100%;
        }

        th.channel-image {
            width: 120px;
        }

        td.channel-detail {
            padding: 10px;
        }

        .channel-desc {
            padding: 10px 0;
        }

        .channel-name {
            font-weight: bold;
        }

        ul.channel-content {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        td.reallyOrder {
            width: 30px;
            text-align: center;
            vertical-align: middle;
        }

        td.channel-order, td.actions {
            vertical-align: middle;
            text-align: center;
        }

        input#display-enabled-only {
            vertical-align: top;
            margin-right: 5px;
        }
    </style>
</head>
<body>

<label for="display-enabled-only" class="pull-left" onclick="toggleEnabledOnly()"><input type="checkbox"
                                                                                         id="display-enabled-only">只显示已启用的</label>

<a href="${ctx}/admin/channels/create" class="pull-right">+ 新建</a>

<table id="contentTable" class="table table-bordered table-condensed table-hover">
    <thead>
    <tr>
        <th class="reallyOrder"></th>
        <th class="channel-image">预览</th>
        <th>详细</th>
        <th class="input-mini">编号</th>
        <th class="input-medium">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${channels}" var="channel" varStatus="varStatus">
        <tr class="${channel.enabled == true? "enabled" : ""}">
            <td class="reallyOrder">
            </td>
            <td class="channel-image">
                <div style="background-color: ${channel.backgroundColor}">
                    <img class="input-small" src="${channel.imageUrl}"/>

                    <div class="display-name">${channel.title}</div>
                </div>
            </td>
            <td class="channel-detail">
                <div class="channel-name">${channel.name}</div>
                <div class="channel-desc">${empty channel.description ? "<i>无描述</i>" : channel.description}</div>
                <ul class="channel-content">
                    <li>${empty channel.follow ? "<i>无follow</i>" : "follow "}${channel.follow}</li>
                    <li>${empty channel.track ? "<i>无track</i>" : "track "}${channel.track}</li>
                </ul>
            </td>
            <td class="channel-order">${channel.order}</td>
            <td class="actions">
                <div class="btn-group">
                    <button class="btn"
                            onclick="updateChannelStatus('${channel.id}', this)">${channel.enabled ? "禁用" : "启用"}</button>
                    <button class="btn" onclick="location.href='${ctx}/admin/channels/update/${channel.id}'">编辑</button>
                    <button class="btn"
                            onclick="if(confirm('删除 '+ $(this).parents('tr').find('.channel-name').html() +' 频道?')) $('#deleteChannelForm').attr('action', '${ctx}/admin/channels/delete/${channel.id}').submit()">
                        删除
                    </button>
                </div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<form id="deleteChannelForm" method="post" action="" style="display: none"></form>

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
    function updateChannelStatus(id, btn) {
        var action = $(btn).html() == '启用' ? 'enableChannel' : 'disableChannel';

        $.ajax({
            url: '${ctx}/admin/channels/' + action + '/' + id,
            type: 'post',
            success: function (data) {
                if (data == 'ok') {
                    if (action == 'enableChannel') {
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
