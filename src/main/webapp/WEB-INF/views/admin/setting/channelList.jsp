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

        td.channel-preview, th.channel-preview{
            width: 100px;
        }

        .channel-preview > .channel-button {
            width: 95px;
            height: 95px;
            overflow: hidden;
            margin: 10px;
            position: relative;
            text-align: center;
        }

        .channel-preview .channel-subject {
            position: absolute;
            top: 10px;
            width: 100%;
        }

        .channel-preview img{
            width: 95px;
        }

        .channel-detail {
            padding: 10px;
        }

        .channel-desc {
            padding: 3px 0;
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
        <th class="channel-preview">预览</th>
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
            <td class="channel-preview">
                <div class="channel-button" style="background-color: ${channel.backgroundColor}">
                    <div class="channel-subject">${channel.title}</div>
                    <img src="${channel.imageUrl}"/>
                </div>
            </td>
            <td class="channel-detail">
                <div class="channel-name" title="频道名">${channel.name}</div>
                <div class="channel-desc">描述: ${empty channel.description ? "<i>无</i>" : channel.description}</div>
                <ul class="channel-content">
                    <li>follow: ${empty channel.follow ? "<i>无</i>" : channel.follow}</li>
                    <li>track: ${empty channel.track ? "<i>无</i>" : channel.track}</li>
                    <c:if test="${empty channel.topPostId}">
                        <li>置顶: <i>无</i></li>
                    </c:if>
                    <c:if test="${!empty channel.topPostId}">
                        <li>置顶: <a href="${ctx}/play/${channel.topPostId}" target="_blank">${channel.topPostId}</a></li>
                    </c:if>
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