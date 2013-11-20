<%@ page import="com.lebo.rest.ImRestController" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="maxCount" value="<%=ImRestController.RECENT_MAX_COUNT%>"/>

<tags:form name="获取未读消息" method="GET" action="${ctx}/api/1.1/ims/unread.json">
    <ul>
        <li>返回当前登录用户的未读消息，最多 ${maxCount} 条</li>
        <li>系统会将<strong>全部</strong>未读消息标记为已读</li>
    </ul>
    <p>
        是否轮询 <code>true</code>/<code>false</code>
    </p>
    <ul>
        <li>如果是定时轮询传true，如果是收到系统推送通知查询未读消息传false</li>
        <li>服务端可能会统计轮询，或针对轮询做优化处理</li>
    </ul>
    <tags:field name="polling" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1.1/ims/unread.json?polling=true">
    [
        {
            id: "528616dd1a88005182b89112",
            fromUserId: "52356929343539a89a52dc8d",
            toUserId: "5274a4ff1a882a098ff02899",
            message: "文字消息",
            type: 1,
            createdAt: "Fri Nov 15 20:43:09 +0800 2013",
            messageTime: 1384519378,
            unread: true
        }
    ]
</tags:example>