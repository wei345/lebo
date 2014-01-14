<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="In-App Purchase交付金币" method="POST" action="${ctx}/api/1.1/vg/inAppPurchase.json">

    <tags:textarea name="receiptData"/>
    <tags:field name="userId"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1.1/vg/inAppPurchase.json">
    {
        id: 1,
        name: "10个金币",
        description: "金币是一种全部或大部份由黄金制造的硬币。",
        price: 1,
        priceUnit: "CNY",
        discount: 0,
        cost: 1,
        imageUrl: "http://file.dev.lebooo.com/images/gold/gold-10.png",
        quantity: 1
    }
</tags:example>