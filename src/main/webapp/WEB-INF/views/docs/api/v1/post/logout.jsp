<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="登出" method="POST" action="${ctx}/api/v1/logout.json">
    <p>没有参数</p>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/v1/logout.json">
    {
    "name": "法图_麦",
    "profileImageUrl": "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
    "provider": "weibo",
    "screenName": "法图_麦",
    "token": "2.00vHLEwBz7QwTCbafc736d580QUCCY",
    "uid": "1774156407"
    }
</tags:example>