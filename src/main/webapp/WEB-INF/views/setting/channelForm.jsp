<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>新建频道</title>

    <script>
        $(document).ready(function () {
            //聚焦第一个输入框
            $("#id").focus();
            //为inputForm注册validate函数
            $("#inputForm").validate();
        });
    </script>
</head>

<body>

<form id="inputForm" action="${ctx}/admin/channels/create" method="post" class="form-horizontal"
      enctype="multipart/form-data">
    <fieldset>
        <legend>
            <small>新建频道</small>
        </legend>

        <div class="control-group">
            <label for="id" class="control-label">ID:</label>
            <div class="controls">
                <input type="text" id="id" name="id" value="${id}" class="input-large required"/>
            </div>
        </div>

        <div class="control-group">
            <label for="name" class="control-label">名称:</label>
            <div class="controls">
                <input type="text" id="name" name="name" value="${name}" class="input-large"/>
            </div>
        </div>

        <div class="control-group">
            <label for="description" class="control-label">描述:</label>
            <div class="controls">
                <input type="text" id="description" name="description" value="${description}" class="input-large"/>
            </div>
        </div>

        <div class="control-group">
            <label for="channelImage" class="control-label">图片:</label>
            <div class="controls">
                <input type="file" id="channelImage" name="channelImage" value="${channelImage}" class="input-large" minlength="3"/>
            </div>
        </div>

        <div class="control-group">
            <label for="backgroundColor" class="control-label">背景:</label>
            <div class="controls">
                <input type="text" id="backgroundColor" name="backgroundColor" value="${backgroundColor}"
                       class="input-large"/>
            </div>
        </div>

        <div class="control-group">
            <label for="track" class="control-label">follow:</label>
            <div class="controls">
                <input type="text" id="follow" name="follow" value="${follow}"
                       class="input-large"/>
            </div>
        </div>

        <div class="control-group">
            <label for="track" class="control-label">track:</label>
            <div class="controls">
                <input type="text" id="track" name="track" value="${track}" class="input-large"/>
            </div>
        </div>

        <div class="control-group">
            <label for="enabled" class="control-label">启用:</label>
            <div class="controls">
                <input type="checkbox" id="enabled" name="enabled" ${enabled == null ? "" : "checked='checked'"}/>
            </div>
        </div>

        <div class="form-actions">
            <input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
            <input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;
        </div>
    </fieldset>
</form>
</body>
</html>