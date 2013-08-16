<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="取消转发视频" method="POST" action="${ctx}/api/1/statuses/unrepost.json">
    <tags:field name="id" value=""/>
</tags:form>

<tags:example method="POST" url="">

</tags:example>
