<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="发布评论" method="POST" action="${ctx}/api/1/comments/destroy.json">
    <tags:field name="id" value=""/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1/comments/destroy.json?id=520882901a888e28c34607ac">
    {
        id: "520882901a888e28c34607ac",
        postId: "51f0c8671a8824c465355d59",
        createdAt: "Mon Aug 12 14:37:04 +0800 2013",
        text: "测试删除评论",
        files: [ ],
        hasVideo: false
    }
</tags:example>