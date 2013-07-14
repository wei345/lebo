<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="搜索Tags" method="GET" action="${ctx}/api/1/hashtags/search.json">
    <tags:field name="q" value="标签"/>
    <tags:field name="count" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/hashtags/search.json?q=%E6%A0%87%E7%AD%BE">
    [
        {
            name: "标签3",
            count: 2
        },
        {
            name: "标签1",
            count: 1
        },
        {
            name: "标签2",
            count: 1
        },
        {
            name: "标签4",
            count: 1
        }
    ]
</tags:example>