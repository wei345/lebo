<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>用户管理</title>

    <script>
        $(document).ready(function () {
            //聚焦第一个输入框
            $("#name").focus();
            //为inputForm注册validate函数
            $("#inputForm").validate();
        });
    </script>
</head>

<body>
<form id="inputForm" action="${ctx}/admin/user/update" method="post" class="form-horizontal">
    <input type="hidden" name="id" value="${user.id}"/>
    <fieldset>
        <legend>
            <small>用户管理</small>
        </legend>
        <div class="control-group">
            <label class="control-label">头像:</label>

            <div class="controls">
                <img src="${user.profileImageUrl}" title="用户头像"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">用户名:</label>

            <div class="controls">
                <span class="help-inline">${user.screenName}</span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">姓名:</label>

            <div class="controls">
                <input type="text" id="name" name="name" value="${user.name}" class="input-large required"/>
            </div>
        </div>
        <div class="control-group">
            <label for="plainPassword" class="control-label">密码:</label>

            <div class="controls">
                <input type="password" id="plainPassword" name="plainPassword" class="input-large"
                       placeholder="...Leave it blank if no change"/>
            </div>
        </div>
        <div class="control-group">
            <label for="confirmPassword" class="control-label">确认密码:</label>

            <div class="controls">
                <input type="password" id="confirmPassword" name="confirmPassword" class="input-large"
                       equalTo="#plainPassword"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">注册日期:</label>

            <div class="controls">
                <span class="help-inline" style="padding:5px 0px"><fmt:formatDate value="${user.createdAt}"
                                                                                  pattern="yyyy年MM月dd日  HH时mm分ss秒"/></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">最后登录日期:</label>

            <div class="controls">
                <span class="help-inline" style="padding:5px 0px"><fmt:formatDate value="${user.lastSignInAt}"
                                                                                  pattern="yyyy年MM月dd日  HH时mm分ss秒"/></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">签名:</label>
            <div class="controls">
                <span class="help-inline">${user.description}</span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">新浪加V用户:</label>
            <div class="controls">
                <span class="help-inline">${user.weiboVerified ? "是" : "否"}</span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">性别:</label>
            <div class="controls">
                <span class="help-inline">${user.gender == 1 ? '男' : (user.gender == 0 ? '女' : '未知')}</span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">粉丝数:</label>
            <div class="controls">
                <span class="help-inline">${user.followersCount}</span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">收到红心数:</label>
            <div class="controls">
                <span class="help-inline">${user.beFavoritedCount}</span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">所有帖子被浏览数:</label>
            <div class="controls">
                <span class="help-inline">${user.viewCount}</span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">精品数:</label>
            <div class="controls">
                <span class="help-inline">${user.digestCount}</span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">帖子数:</label>
            <div class="controls">
                <span class="help-inline">${user.statusesCount}</span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">关注数:</label>
            <div class="controls">
                <span class="help-inline">${user.friendsCount}</span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">weiboToken:</label>
            <div class="controls">
                <span class="help-inline">${user.weiboToken}</span>
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
