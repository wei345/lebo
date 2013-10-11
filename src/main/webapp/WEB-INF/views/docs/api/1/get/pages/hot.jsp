<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="热门" action="${ctx}/api/1/pages/hot.json" method="GET">
    <tags:fields-page-size/>
    <p>
        返回的结果对象中是否带有广告，<code>true</code>或<code>false</code>。
    </p>
    <tags:field name="ads" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/pages/hot.json">

</tags:example>