<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>新建频道</title>

    <script>
        $(document).ready(function() {
            //聚焦第一个输入框
            $("#name").focus();
            //为inputForm注册validate函数
            $("#inputForm").validate();
        });
    </script>
</head>

<body>

<form id="inputForm" action="${ctx}/admin/channels/create" method="post" class="form-horizontal" enctype="multipart/form-data">
    <fieldset>
        <legend><small>新建频道</small></legend>

        <div class="control-group">
            <label for="name" class="control-label">名称:</label>
            <div class="controls">
                <input type="text" id="name" name="name" value="${name}" class="input-large required"/>
            </div>
        </div>
        <div class="control-group">
            <label for="content" class="control-label">内容:</label>
            <div class="controls">
                <input type="text" id="content" name="content" value="${content}" class="input-large required"/>
            </div>
        </div>
        <div class="control-group">
            <label for="image" class="control-label">图片:</label>
            <div class="controls">
                <input type="file" id="image" name="image" class="input-large required" minlength="3"/>
            </div>
        </div>
        <div class="control-group">
            <label for="backgroundColor" class="control-label">背景:</label>
            <div class="controls">
                <input type="text" id="backgroundColor" name="backgroundColor" value="${backgroundColor}" class="input-large required"/>
            </div>
        </div>
        <div class="control-group">
            <label for="enabled" class="control-label">启用:</label>
            <div class="controls">
                <input type="checkbox" id="enabled" name="enabled" ${enabled == null ? "" : "checked='checked'"}/>
            </div>
        </div>
        <div class="form-actions">
            <input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;
            <input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
        </div>
    </fieldset>
</form>
</body>
</html>