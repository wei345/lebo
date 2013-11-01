<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="金币商品" method="GET" action="${ctx}/api/1.1/ec/goldProducts.json">

</tags:form>

<tags:example method="GET" url="http://192.168.1.103:8080/api/1.1/ec/goldProducts.json">

</tags:example>