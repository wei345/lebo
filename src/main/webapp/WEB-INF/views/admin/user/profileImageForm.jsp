<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>用户管理</title>
</head>
<body>
<form id="inputForm" action="${ctx}/admin/user/updateProfileImage/${user.id}" method="post" class="form-horizontal"
      enctype="multipart/form-data">

    <fieldset>
        <legend>
            <small>修改头像</small>
        </legend>

        <div class="control-group">
            <label class="control-label">用户名:</label>

            <div class="controls">
                <span class="help-inline">${user.screenName}</span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">头像:</label>

            <div class="controls">
                <input type="file" name="profileImage"/>
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