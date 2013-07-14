<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="发布评论" method="POST" action="${ctx}/api/1/comments/create.json" enctype="multipart/form-data">
    <tags:field name="postId" value="51de1c3c1a88b82f949585b4"/>
    <tags:textarea name="text" value="中文abc"/>
    <tags:field name="video" type="file" optional="true"/>
    <tags:field name="image" type="file" optional="true"/>
</tags:form>