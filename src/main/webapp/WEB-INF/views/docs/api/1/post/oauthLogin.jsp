<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="OAuth登录" method="POST" action="${ctx}/api/1/oauthLogin.json">
    <p>
        在设计上支持多平台OAuth登录，目前只启用了新浪微博。provider值为weibo。
    </p>
    <tags:field name="provider" value="weibo"/>
    <tags:field name="token" value="2.00vHLEwBz7QwTCbafc736d580QUCCY"/>
</tags:form>

<tags:example method="POST" url="/api/1/oauthLogin.json?provider=weibo&token=2.00vHLEwBz7QwTCbafc736d580QUCCY">
    {
        "name": "法图_麦",
        "profileImageUrl": "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
        "provider": "weibo",
        "screenName": "法图_麦",
        "token": "2.00vHLEwBz7QwTCbafc736d580QUCCY",
        "uid": "1774156407"
    }
</tags:example>