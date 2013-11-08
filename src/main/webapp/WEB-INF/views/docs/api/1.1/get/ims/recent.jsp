<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="最近消息" method="GET" action="${ctx}/api/1.1/ims/recent.json">
    <p>
        在这个时间之后的最近消息。
        秒数，如 1383826364。
    </p>
    <tags:field name="fromTime"/>
</tags:form>


<tags:example method="GET" url="http://localhost:8080/api/1.1/ims/recent.json?fromTime=1383826364">
    [
        {
            id: "527c67e41a88858af9a5e3ed",
            fromUserId: "52356929343539a89a52dc8d",
            toUserId: "52356929343539a89a52dc8d",
            message: "http://file.dev.lebooo.com/im/527c648a1a884c28e939d568-0.mp4,http://file.dev.lebooo.com/im/527c648a1a884c28e939d568-1.jpg",
            type: 2,
            createdAt: "Fri Nov 08 12:26:12 +0800 2013"
        }
    ]
</tags:example>