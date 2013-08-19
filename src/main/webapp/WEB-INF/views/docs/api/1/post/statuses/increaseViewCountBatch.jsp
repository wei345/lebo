<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="视频播放次数+1" method="POST" action="${ctx}/api/1/statuses/increaseViewCountBatch.json">
    <p>
        一个或多个帖子ID，多个帖子ID之间以逗号(,)分隔
    </p>
    <tags:field name="postIds"/>
    <p>
        一个或多个用户ID，多个用户ID之间以逗号(,)分隔。用户ID与顺序帖子ID对应，用户是帖子的作者。
    </p>
    <tags:field name="userIds"/>
</tags:form>

<p>
    响应body: 无
</p>