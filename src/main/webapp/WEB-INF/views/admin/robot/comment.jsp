<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>机器人评论</title>
    <style type="text/css">
        .multi-select-left {
            float: left;
        }

        .multi-select-left .title, .multi-select-right .title {
            display: block;
        }

        .multi-select-btn {
            width: 5em;
            padding: 2em;
            float: left;
        }

        .multi-select-btn input[type=button], .multi-select-btn button {
            margin-top: 0.5em;
            display: block;
        }

        #previewTable {
            margin-top: 1em;
        }
    </style>
    <script>
        $(document).ready(function () {
            <c:if test="${param.postId == null}">
            //聚焦第一个输入框
            $("#postId").focus();
            </c:if>

            $('#submitBtn').hide();

            //为inputForm注册validate函数
            $("#inputForm").validate();

            //双向选择列表双击事件
            $('#robots-unchosen').dblclick(function () {
                multiSelectAdd(this, '#robots-chosen');
                updateRobotCount();
            });

            $('#robots-chosen').dblclick(function () {
                multiSelectAdd(this, '#robots-unchosen');
                updateRobotCount();
            });

            $('#sayings-unchosen').dblclick(function () {
                multiSelectAdd(this, '#sayings-chosen');
                updateSayingsCount();
            });

            $('#sayings-chosen').dblclick(function () {
                multiSelectAdd(this, '#sayings-unchosen');
                updateSayingsCount();
            });

            //创建随机选择按钮
            addRandomButton('#btn-robot-random', ${fn:length(robots)}, function (num) {
                return '<li><a href="javascript:addRandomRobot(' + num + ');void(0)">' + num + '</a></li>';
            });
            addRandomButton('#btn-sayings-random', ${fn:length(sayings)}, function (num) {
                return '<li><a href="javascript:addRandomSayings(' + num + ');void(0)">' + num + '</a></li>';
            });
        });
    </script>
    <script src="${ctx}/static/admin/robot/comment.js"></script>
</head>
<body>

<h3>帖子</h3>

<form id="inputForm" action="${ctx}/admin/robot/comment" method="POST">
    <input type="text" id="postId" name="postId" value="${postId}" placeholder="帖子ID"
           class="required input-xlarge"/>
    <input type="hidden" name="comments" value=""/>
</form>

<h3>机器人</h3>

<div class="multi-select-left">
    <span class="title">候选(<span id="robots-unchosen-count">${fn:length(robots)}</span>):</span>
    <select id="robots-unchosen" size="14" multiple="multiple">
        <c:forEach items="${robots}" var="item">
            <option value="${item.id}"
                    group="<c:forEach items="${item.robot.groups}" var="group">${group},</c:forEach>">${item.screenName}</option>
        </c:forEach>
    </select>
</div>

<div class="multi-select-btn">
    <input type="button" value="&gt;" class="btn"
           onclick="multiSelectAdd($('#robots-unchosen'),$('#robots-chosen')); updateRobotCount();"/>

    <div id="btn-robot-random" class="btn-group">
        <button class="btn dropdown-toggle" data-toggle="dropdown">
            随机
            <span class="caret"></span>
            &gt;
        </button>
        <ul class="dropdown-menu">
        </ul>
    </div>

    <div id="btn-robot-group" class="btn-group">
        <button class="btn dropdown-toggle" data-toggle="dropdown">
            组
            <span class="caret"></span>
            &gt;
        </button>
        <ul class="dropdown-menu">
            <c:forEach items="${robotGroups}" var="group">
                <li><a href="javascript:addGroupRobot('${group.name}'); void(0)">${group.name}</a></li>
            </c:forEach>
        </ul>
    </div>

    <input type="button" value="&gt;&gt;" class="btn"
           onclick="multiSelectAddAll($('#robots-unchosen'),$('#robots-chosen')); updateRobotCount();"/>

    <input type="button" value="&lt;" class="btn"
           onclick="multiSelectAdd($('#robots-chosen'),$('#robots-unchosen')); updateRobotCount();"/>

    <input type="button" value="&lt;&lt;" class="btn"
           onclick="multiSelectAddAll($('#robots-chosen'),$('#robots-unchosen')); updateRobotCount();"/>
</div>

<div class="multi-select-right">
    <span class="title">已选(<span id="robots-chosen-count">0</span>):</span>
    <select id="robots-chosen" name="robots-chosen" size="14" multiple="multiple">
    </select>
</div>


<h3>语句</h3>

<div class="multi-select-left">
    <span class="title">候选(<span id="sayings-unchosen-count">${fn:length(sayings)}</span>):</span>
    <select id="sayings-unchosen" size="14" multiple="multiple">
        <c:forEach items="${sayings}" var="item">
            <option value="${item.id}"
                    tag="<c:forEach items="${item.tags}" var="tag">${tag},</c:forEach>">${item.text}</option>
        </c:forEach>
    </select>
</div>

<div class="multi-select-btn">
    <input type="button" value="&gt;" class="btn"
           onclick="multiSelectAdd($('#sayings-unchosen'),$('#sayings-chosen')); updateSayingsCount();"/>

    <div id="btn-sayings-random" class="btn-group">
        <button class="btn dropdown-toggle" data-toggle="dropdown">
            随机
            <span class="caret"></span>
            &gt;
        </button>
        <ul class="dropdown-menu">
        </ul>
    </div>

    <div id="btn-sayings-group" class="btn-group">
        <button class="btn dropdown-toggle" data-toggle="dropdown">
            标签
            <span class="caret"></span>
            &gt;
        </button>
        <ul class="dropdown-menu">
            <c:forEach items="${sayingTags}" var="group">
                <li><a href="javascript:addTagSayings('${group.name}'); void(0)">${group.name}</a></li>
            </c:forEach>
        </ul>
    </div>

    <input type="button" value="&gt;&gt;" class="btn"
           onclick="multiSelectAddAll($('#sayings-unchosen'),$('#sayings-chosen')); updateSayingsCount();"/>

    <input type="button" value="&lt;" class="btn"
           onclick="multiSelectAdd($('#sayings-chosen'),$('#sayings-unchosen')); updateSayingsCount();"/>

    <input type="button" value="&lt;&lt;" class="btn"
           onclick="multiSelectAddAll($('#sayings-chosen'),$('#sayings-unchosen')); updateSayingsCount();"/>
</div>

<div class="multi-select-right">
    <span class="title">已选(<span id="sayings-chosen-count">0</span>):</span>
    <select id="sayings-chosen" name="sayings-chosen" size="14" multiple="multiple">
    </select>
</div>

<h3>时间</h3>

<input id="minutes" type="text" value="5" class="input-mini"/>分钟内，每个的机器人各发1条评论

<h3>预览</h3>

<input type="button" value="生成预览" class="btn" onclick="preview()"/>

<table id="previewTable" class="table table-condensed table-hover">

</table>

<div class="form-actions">
    <input id="submitBtn" type="button" class="btn btn-primary" value="提交" onclick="submit()"/>
</div>

</body>
</html>