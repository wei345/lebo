<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>网页版REST客户端</title>
</head>

<body>

<hr>

<h4>${ctx}/api/v1/statuses/update</h4>

<form method="POST" action="${ctx}/api/v1/statuses/update" enctype="multipart/form-data">
    <div><textarea name="text" maxlength="140" placeholder="添加描述.." class="required"
                   style="width:540px;height:100px;">${text}</textarea></div>
    <div style="margin-top: 1em;">视频：<input type="file" name="media" class="required"/></div>
    <div><input type="submit" value="发&nbsp;&nbsp;布"
                style="margin-top: 1em; padding:10px 1em; background-color: #76D6FF; border: 1px solid #797979; color: #ffffff; font-size: 18px;"/>
    </div>
</form>

</body>