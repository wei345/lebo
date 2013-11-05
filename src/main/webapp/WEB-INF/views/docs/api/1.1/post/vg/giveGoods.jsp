<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="赠送物品" method="POST" action="${ctx}/api/1.1/vg/giveGoods.json">
    <p>
        <code>toUserId</code>和<code>toScreenName</code>二选一
    </p>
    <tags:field name="toUserId" optional="true"/>
    <tags:field name="toScreenName" optional="true"/>
    <tags:field name="goodsId"/>
    <tags:field name="quantity"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1.1/vg/giveGoods.json">
    {
        ok: true
    }
</tags:example>