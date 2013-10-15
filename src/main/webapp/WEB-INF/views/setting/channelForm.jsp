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

<a href="${ctx}/admin/channels">= 频道列表</a>

<form id="inputForm" action="${ctx}/admin/channels/update" method="post" class="form-horizontal"
      enctype="multipart/form-data">
    <c:if test="${channel.id != null}">
        <input type="hidden" name="id" value="${channel.id}"/>
    </c:if>
    <fieldset>
        <legend>
            <small>新建频道</small>
        </legend>

        <div class="control-group">
            <label for="name" class="control-label">频道名:</label>

            <div class="controls">
                <input type="text" id="name" name="name" value="${channel.name}" class="input-large required"/>
            </div>
        </div>

        <div class="control-group">
            <label for="title" class="control-label">显示名:</label>

            <div class="controls">
                <input type="text" id="title" name="title" value="${channel.title}" class="input-large"/>
            </div>
        </div>

        <div class="control-group">
            <label for="channelImage" class="control-label">
                <input type="checkbox" onclick="if(this.checked){$('#channelImage, #slug').removeAttr('disabled')}else{$('#channelImage, #slug').attr('disabled', 'disabled')}"/>
                图片:
            </label>

            <div class="controls">
                <input type="file" id="channelImage" name="channelImage" value="${channelImage}" class="input-large"
                       minlength="3" ${channel.id == null ? "" : "disabled"}/>
            </div>
        </div>

        <div class="control-group">
            <label for="slug" class="control-label">图片 slug:</label>
            <div class="controls">
                <input type="text" id="slug" name="slug" value="${channel.slug}" class="input-large" ${channel.id == null ? "" : "disabled"}/>
                由小写字母、数字、连字符(-)组成
            </div>
        </div>

        <div class="control-group">
            <label for="description" class="control-label">描述(可选):</label>

            <div class="controls">
                <input type="text" id="description" name="description" value="${channel.description}"
                       class="input-large"/>
                频道详情顶部内容
            </div>
        </div>

        <div class="control-group">
            <label for="backgroundColor" class="control-label">背景颜色:</label>

            <div class="controls">
                <input type="text" id="backgroundColor" name="backgroundColor" value="${channel.backgroundColor}"
                       class="input-large"/>
                如#abcdef, 不能使用颜色名
            </div>
        </div>

        <div class="control-group">
            <label for="follow" class="control-label">follow:</label>

            <div class="controls">
                <input type="text" id="follow" name="follow" value="${channel.follow}"
                       class="input-large"/>
            </div>
        </div>

        <div class="control-group">
            <label for="track" class="control-label">track:</label>

            <div class="controls">
                <input type="text" id="track" name="track" value="${channel.track}" class="input-large"/>
            </div>
        </div>

        <div class="control-group">
            <label for="order" class="control-label">顺序:</label>

            <div class="controls">
                <input type="text" id="order" name="order" value="${channel.order}" class="input-large"/>
                任意整数。列表按此字段排序，由小到大.
            </div>
        </div>

        <div class="control-group">
            <label for="enabled" class="control-label">启用:</label>

            <div class="controls">
                <input type="checkbox" id="enabled"
                       name="enabled" ${channel.enabled == null ? "" : "checked='checked'"}/>
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
