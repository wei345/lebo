<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="发布视频" method="POST" action="${ctx}/api/v1/statuses/update.json" enctype="multipart/form-data">
    <tags:field name="video" type="file" />
    <tags:field name="image" type="file" />
    <tags:field name="source" value="乐播网页版"/>
    <tags:textarea name="text" value="视频"/>
</tags:form>
