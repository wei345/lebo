<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="搜索Tags" method="GET" action="${ctx}/api/1/hashtags/search.json">
    <tags:field name="q" value="标签" optional="true"/>
    <tags:field name="page" optional="true"/>
    <tags:field name="size" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/hashtags/search.json?q=%E6%A0%87%E7%AD%BE">
    [
        {
            id: "51f8d50e81df74bcee25d481",
            name: "番石榴",
            count: 2,
            increaseAt: "Wed Jul 31 17:40:26 +0800 2013"
        },
        {
            id: "51f8db8a81df74bcee25d482",
            name: "獴狐猴",
            count: 1,
            increaseAt: "Wed Jul 31 17:40:26 +0800 2013"
        }
    ]
</tags:example>