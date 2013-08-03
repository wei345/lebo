<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="更新APNS token" method="POST" action="${ctx}/api/1/account/updateApnsToken.json">
    <tags:field name="apnsDevelopmentToken" optional="true"/>
    <tags:field name="apnsProductionToken" optional="true"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1/account/updateApnsToken.json?apnsDevelopmentToken=test">
    {
        id: "51def1e61a883914869e46f3",
        apnsProductionToken: "test2",
        apnsDevelopmentToken: "test"
    }
</tags:example>
