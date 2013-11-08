<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="最近消息" method="GET" action="${ctx}/api/1.1/ims/recent.json">
    <p>
        在这个时间之后的最近消息。
        秒数，如 1383826364。
    </p>
    <tags:field name="afterTime"/>
</tags:form>


<tags:example method="GET" url="http://localhost:8080/api/1.1/ims/recent.json?afterTime=1383826364">
    [
        {
            id: "527cac8b1a88fa8b6685af92",
            fromUserId: "52356929343539a89a52dc8d",
            toUserId: "52356929343539a89a52dc8d",
            message: "test",
            type: 1,
            createdAt: "Fri Nov 08 17:19:07 +0800 2013",
            messageTime: 1383901962
        }
    ]
</tags:example>