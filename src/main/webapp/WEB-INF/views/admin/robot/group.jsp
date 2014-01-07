<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>机器人分组</title>
</head>
<body>

<div class="pageinfo">共 ${fn:length(list)} 条</div>

<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th style="width:8em;">组名</th>
        <th style="width:8em;">成员数</th>
        <th style="width:11em;">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${list}" var="item">
        <tr>
            <td>${item.name}</td>
            <td>${item.memberCount}</td>
            <td>
                <a href="${ctx}/admin/user?robot=true&robotGroup=${item.name}">查看成员</a>
                <button class="btn" onclick="renameRobotGroup('${item.name}', this)">修改组名</button>
                <button class="btn" onclick="deleteRobotGroup('${item.name}', this)">删除组</button>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<div id="rename-group-modal" class="modal hide fade">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>重命名机器人分组 / <span id="group-name"></span></h3>
    </div>
    <div class="modal-body">
        <input id="group-name-input" type="text" value=""/>

        <div class="modal-alert-error"></div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-primary" onclick="doRenameRobotGroup()">保存</button>
    </div>
</div>

<script>
    var allGroups = ${allGroupsJson};
    var oldName;
    var nameContainer;

    function deleteRobotGroup(name, btn) {
        if(confirm('确定删除 ' + name + ' 吗？')){
            $.ajax({
                url: '${ctx}/admin/robot/deleteGroup',
                type: 'POST',
                data: {name: name},
                success: function(data){
                    if(data == 'ok'){
                        $(btn).parents('tr').remove();
                    }
                }
            });
        }
    }

    function renameRobotGroup(name, btn) {
        oldName = name;
        nameContainer = $(btn).parents('tr').find('td:first');

        $('#modal-alert-error').hide();

        $('#group-name').html(name);

        $('#group-name-input').val(name);

        $('#rename-group-modal').modal();
    }

    function doRenameRobotGroup() {
        var newName = $('#group-name-input').val();

        var newNameIndex = allGroups.indexOf(newName);
        if(newNameIndex > -1 && !confirm(newName + ' 已存在，' + oldName + ' 成员将合并到 ' + newName + '，确定修改吗？')){
            return;
        }

        $.ajax({
            url: '${ctx}/admin/robot/renameGroup',
            type: 'POST',
            data: {oldName: oldName, newName: newName},
            success: function (data) {
                if (data == 'ok') {
                    $('#rename-group-modal').modal('hide');
                    nameContainer.html(newName);
                } else {
                    $('#modal-alert-error').html(data).show();
                }
            },
            error: function(jqXHR){
                $('#modal-alert-error').html(jqXHR.responseText).show();
            }
        });
    }

    $('#rename-group-modal').on('shown', function () {
        $('#group-name-input').select();
    });

    $('#group-name-input').keydown(function (e) {
        if (e.keyCode == 13) {
            doRenameRobotGroup();
        }
    });
</script>

</body>
</html>