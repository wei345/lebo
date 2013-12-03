<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="赠送物品" method="POST" action="${ctx}/api/1.1/vg/giveGoods.json">
    <p>
        原始帖子ID
    </p>
    <tags:field name="postId"/>
    <tags:field name="goodsId"/>
    <tags:field name="quantity"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1.1/vg/giveGoods.json">
    {
        ok: true
    }
</tags:example>