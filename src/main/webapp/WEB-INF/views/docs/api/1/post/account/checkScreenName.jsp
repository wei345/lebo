<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="检查screenName是否可用" method="POST" action="${ctx}/api/1/account/checkScreenName.json">
    <tags:field name="screenName"/>
</tags:form>

如果可用返回true，否则返回false。
