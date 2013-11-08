<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="保存新消息" method="POST" action="${ctx}/api/1.1/ims/new.json">
    <tags:field name="toUserId"/>
    <tags:textarea name="message" value=""/>
    <p>
        整数，表示不同消息类型，由客户端定。
    </p>
    <tags:field name="type"/>
    <p>
        客户端发送消息时间，整数。如 1383901962
    </p>
    <tags:field name="messageTime"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1.1/ims/new.json?toUserId=52356929343539a89a52dc8d&message=test&type=1&messageTime=1383901962">
    {
        id: "527cac8b1a88fa8b6685af92",
        fromUserId: "52356929343539a89a52dc8d",
        toUserId: "52356929343539a89a52dc8d",
        message: "test",
        type: 1,
        createdAt: "Fri Nov 08 17:19:07 +0800 2013",
        messageTime: 1383901962
    }
</tags:example>

