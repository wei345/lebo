<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>用户注册</title>

    <script>
        $(document).ready(function () {
            //聚焦第一个输入框
            $("#loginName").focus();
            //为inputForm注册validate函数
            $("#inputForm").validate({
                rules: {
                    email: {
                        remote: "${ctx}/register/checkEmail",
                        email: true
                    },
                    screenName: {
                        remote: "${ctx}/register/checkScreenName"
                    }
                },
                messages: {
                    email: {
                        remote: "邮件地址已存在"
                    },
                    screenName: {
                        remote: "用户名已存在"
                    }
                }
            });
        });
    </script>
</head>

<body>
<form id="inputForm" action="${ctx}/register" method="post" class="form-horizontal">
    <fieldset>
        <legend>
            <small>用户注册</small>
        </legend>

        <div class="control-group">
            <label for="name" class="control-label">姓名:</label>

            <div class="controls">
                <input type="text" id="name" name="name" class="input-large required"/>
            </div>
        </div>
        <div class="control-group">
            <label for="email" class="control-label">邮件地址:</label>

            <div class="controls">
                <input type="text" id="email" name="email" class="input-large required"/>
            </div>
        </div>
        <div class="control-group">
            <label for="screenName" class="control-label">用户名:</label>

            <div class="controls">
                <input type="text" id="screenName" name="screenName" class="input-large required" minlength="3"/>
            </div>
        </div>
        <div class="control-group">
            <label for="plainPassword" class="control-label">密码:</label>

            <div class="controls">
                <input type="password" id="plainPassword" name="plainPassword" class="input-large required"/>
            </div>
        </div>
        <div class="control-group">
            <label for="confirmPassword" class="control-label">确认密码:</label>

            <div class="controls">
                <input type="password" id="confirmPassword" name="confirmPassword" class="input-large required"
                       equalTo="#plainPassword"/>
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
