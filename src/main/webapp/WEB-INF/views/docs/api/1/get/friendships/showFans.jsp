<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="查看粉丝" action="${ctx}/api/1/friendships/showFans.json" method="GET">
    <tags:field name="userId" value="51e6124ea0eedbd1aad37b6b"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/frinedships/showFans.json?userId=51e6124ea0eedbd1aad37b6b">
   []
</tags:example>