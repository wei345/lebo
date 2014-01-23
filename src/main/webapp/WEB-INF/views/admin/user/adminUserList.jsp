<%@ page import="com.lebo.entity.User" %>
<%@ page import="com.lebo.web.admin.UserAdminController" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>用户管理</title>
    <script>
        var userId2Groups = {};
        var allGroups = ${allGroupsJson};
    </script>
</head>

<body>

<form id="searchForm" class="form-search pull-left" method="GET" action="">

    <input type="search" class="input-large search-query" id="q" name="q" value="${q}" placeholder="用户名，支持正则表达式"
           title="用户名，支持正则表达式">
    <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
          onclick="$('input[name=q]').val('')"></span>

    <input type="search" class="input-large search-query" id="userId" name="userId" value="${param.userId}"
           placeholder="用户ID">
    <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
          onclick="$('input[name=userId]').val('')"></span>

    <label>
        <input type="checkbox" name="robot" value="true"
               <c:if test="${param.robot}">checked</c:if>
               onclick="$('select[name=robotGroup]').attr('disabled', !this.checked)"/>
        机器人
    </label>

    <select name="robotGroup" class="input-small" <c:if test="${!param.robot}">disabled</c:if>>
        <option value="">机器人组</option>
        <c:forEach items="${allGroups}" var="groupName">
            <option value="${groupName}">${groupName}</option>
        </c:forEach>
    </select>

    <div style="padding-top: 10px;">
        <select name="orderBy" class="input-medium">
            <option value="_id">注册时间</option>
            <option value="followersCount">粉丝数</option>
            <option value="beFavoritedCount">红心数</option>
            <option value="viewCount">被浏览数</option>
        </select>

        <select class="input-small" name="order">
            <option ${order == "DESC" ? "selected='selected'" : ""} value="DESC">降序</option>
            <option ${order == "ASC" ? "selected='selected'" : ""} value="ASC">升序</option>
        </select>

        <input type="text" class="input-mini" name="size" value="${size}">条/页
        <button type="submit" class="btn" style="margin-left: 2em;">搜索</button>
    </div>
</form>

<a href="${ctx}/register" class="pull-right">+ 添加新用户</a>

<tags:pageinfo page="${page}" spentSeconds="${spentSeconds}"/>

<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th style="width:14em;">ID</th>
        <th style="width:8em;">用户名</th>
        <th>邮件地址</th>
        <th style="width:8em;">姓名</th>
        <th style="width:10em;">注册时间</th>
        <th style="width:10em;">操作</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (User user : (List<User>) request.getAttribute("users")) {
            request.setAttribute("user", user);
            boolean hasGroup = (user.getRobot() != null && user.getRobot().getGroups() != null);
            String robotGroupJson = hasGroup
                    ?
                    UserAdminController.jsonMapper.toJson(user.getRobot().getGroups())
                    :
                    "[]";
    %>
    <tr id="tr-${user.id}">
        <td>
            <c:if test="${user.banned}">
                <span class="icon-ban-circle" title="已禁用"></span>
            </c:if>
            <span class="icon-user" title="机器人" ${user.robot != null ? '' : 'style="display:none"'}></span>
            <a href="${ctx}/admin/user/update/${user.id}">${user.id}</a>
        </td>
        <td>${user.screenName}</td>
        <td>${user.email}</td>
        <td>${user.name}</td>
        <td>
            <fmt:formatDate value="${user.createdAt}" pattern="yyyy年MM月dd日"/>
            <br/>
            <fmt:formatDate value="${user.createdAt}" pattern="HH时mm分ss秒"/>
        </td>
        <td>
            <a href="${ctx}/admin/post/list?userId=${user.id}" target="_blank">查看帖子</a><br/>
            <a href="${ctx}/admin/comment/list?userId=${user.id}" target="_blank">查看评论</a>

            <div>
                <label title="切换是否机器人">
                    <input type="checkbox" onclick="toggleRobot('${user.id}', this)"
                            <c:if test="${user.robot != null}"> checked</c:if>/>
                    机器人
                </label>
                <button id="btn-robot-group-${user.id}" class="btn"
                        onclick="editRobotGroup('${user.id}', '${user.screenName}')"
                        title="机器人分组"
                        <c:if test="${user.robot == null}"> disabled</c:if>>
                    <%=hasGroup ? StringUtils.join(user.getRobot().getGroups(), ",") : "分组"%>
                </button>
                <script>
                    userId2Groups['${user.id}'] = <%=robotGroupJson%>;
                </script>
            </div>

        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

<c:if test="${empty users}">
    <div style="font-style: italic; text-align: center">没有了</div>
</c:if>

<tags:pagination-normal page="${page}" paginationSize="5"/>

<div id="robot-group-modal" class="modal hide fade">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>机器人分组 / <span class="screen-name"></span></h3>
    </div>
    <div class="modal-body">
        <input id="robot-group-input" type="text"/>
        <input type="button" value="添加" class="btn" onclick="addGroup()"/>

        <form id="robot-groups-form">
            <input type="hidden" name="userId" value=""/>

            <div id="robot-groups-container">

            </div>
        </form>

        <div class="alert-error"></div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-primary" onclick="updateGroup()">保存</button>
    </div>
</div>

<script>
    $(document).ready(function () {

        //回填排序字段下拉列表
        $("#searchForm select[name=orderBy] option[value='${orderBy}']").attr("selected", true);

        //回填机器人组
        $("#searchForm select[name=robotGroup] option[value='${param.robotGroup}']").attr("selected", true);

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

        $('#robot-group-input').keyup(function (e) {
            if (e.keyCode == 13) {
                addGroup();
            }
        });

        $('#robot-group-modal').on('shown', function () {
            $('#robot-group-input').focus();
        });
    });


    function updateGroup() {

        var form = $('#robot-groups-form');

        $.ajax({
            url: '${ctx}/admin/robot/updateGroup',
            type: 'POST',
            data: form.serialize(),
            success: function (data) {
                if (data == 'ok') {

                    var modal = $('#robot-group-modal');
                    var userId = form[0].userId.value;

                    var groups = $.map(form.find('input[type=checkbox][name=groups]:checked'), function (item) {
                        return item.value;
                    });
                    userId2Groups[userId] = groups;

                    updateGroupButtonText(userId);

                    modal.modal('hide');

                } else {
                    $('#robot-group-modal .alter-error').html(data).show();
                }
            },
            error: function (jqXHR) {
                $('#robot-group-modal .alert-error').html(jqXHR.responseText).show();
            }
        });
    }


    function toggleRobot(userId, checkbox) {

        if (checkbox.checked) {
            $.ajax({
                url: '${ctx}/admin/robot/set?userId=' + userId,
                type: 'POST',
                success: function (data) {
                    if (data == 'ok') {
                        $('#btn-robot-group-' + userId).removeAttr('disabled');
                        updateGroupButtonText(userId);
                        $('#tr-' + userId).find('.icon-user').show();
                    }
                }
            });
        } else {
            $.ajax({
                url: '${ctx}/admin/robot/unset?userId=' + userId,
                type: 'POST',
                success: function (data) {
                    if (data == 'ok') {
                        $('#btn-robot-group-' + userId).attr('disabled', 'disabled');
                        userId2Groups[userId] = null;
                        updateGroupButtonText(userId);
                        $('#tr-' + userId).find('.icon-user').hide();
                    }
                }
            });
        }
    }


    function editRobotGroup(userId, screenName) {

        var groups = userId2Groups[userId];
        var modal = $('#robot-group-modal');
        var modalBody = modal.find('.modal-body');
        var groupContainer = modalBody.find('#robot-groups-container');

        groupContainer.empty();
        modal.find('.alert-error').hide();
        $('#robot-group-input').val('');

        //用户ID
        $('#robot-groups-form input[name=userId]').val(userId);

        //选中的组名
        if (groups) {
            $.each(groups, function (index, item) {
                groupContainer.append(newGroupLabel(item, true));
            });
        }

        //未选中的组名
        $.each(allGroups, function (index, item) {
            if (!groups || groups.indexOf(item) == -1) {
                groupContainer.append(newGroupLabel(item, false));
            }
        });

        modal.find('.screen-name').html(screenName);

        modal.modal();
    }


    function addGroup() {
        var name = $.trim($('#robot-group-modal input').val());
        if (name == "") return;

        var checkboxContainer = $('#robot-groups-container');

        if (allGroups.indexOf(name) == -1) {
            checkboxContainer.prepend(newGroupLabel(name, true));
            allGroups.push(name);
        } else {
            checkboxContainer.find('input[type=checkbox][value=' + name + ']').attr('checked', 'checked');
        }
    }


    function newGroupLabel(name, checked) {
        return '<label><input name="groups" type="checkbox" value="' + name + '"' + (checked ? ' checked="checked"' : '') + '/>' + name + '</label>';
    }

    function updateGroupButtonText(userId) {
        var groups = userId2Groups[userId] || [];
        $('#btn-robot-group-' + userId).html(groups.length == 0 ? '分组' : groups.join(','));
    }
</script>

</body>
</html>
