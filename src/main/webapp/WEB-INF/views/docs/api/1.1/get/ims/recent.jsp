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

</tags:example>