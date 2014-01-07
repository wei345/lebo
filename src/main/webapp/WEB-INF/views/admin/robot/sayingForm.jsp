<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>机器人语库</title>

    <script>
        $(document).ready(function () {
            //聚焦第一个输入框
            $("#text").focus();
            //为inputForm注册validate函数
            $("#inputForm").validate();
        });
    </script>
</head>
<body>

<a href="${ctx}/admin/robot/saying">= 返回列表</a>

<form id="inputForm" action="" method="post" class="form-horizontal">
    <input type="hidden" name="goToList" value="false" disabled/>
    <c:if test="${saying.id != null}">
        <input type="hidden" name="id" value="${saying.id}"/>
    </c:if>

    <fieldset>
        <legend>
            <small>${saying.id == null ? "添加" : "修改"}</small>
        </legend>

        <div class="control-group">
            <label class="control-label">内容:</label>

            <div class="controls">
                <textarea id="text" name="text" placeholder="内容" class="required" cols="40" rows="4">${saying.text}</textarea>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">标签:</label>

            <div class="controls">
                <input name="tags" type="text" value="${tags}" placeholder="标签"/>
                <span>多个标签以<code>,</code>分隔</span>
            </div>
        </div>

        <div class="form-actions">
            <button class="btn" type="button" onclick="saveAndAdd()">保存 & 继续添加</button>
            <button class="btn btn-primary" type="submit">保存</button>
        </div>
    </fieldset>
</form>

<script>
    function saveAndAdd() {
        var form = document.getElementById("inputForm");
        form.goToList.disabled = false;
        form.submit();
    }
</script>
</body>
</html>