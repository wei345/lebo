<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="视频分享数+1" method="POST" action="${ctx}/api/1.1/statuses/increaseShareCount.json">
    <p>
        帖子ID。必须原帖ID，转发帖ID无效。
    </p>
    <tags:field name="postId"/>
</tags:form>

<tags:example method="POST" url="${ctx}/api/1.1/statuses/increaseShareCount.json?postId=52a542e11a8841b0c325b1b0">
    {
        ok: true
    }
</tags:example>
