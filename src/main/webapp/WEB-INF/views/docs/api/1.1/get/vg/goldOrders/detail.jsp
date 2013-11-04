<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="订单详细" method="GET" action="${ctx}/api/1.1/vg/goldOrders/detail.json">
    <tags:field name="orderId"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1.1/vg/goldOrders/detail.json?orderId=2">
    {
        id: 2,
        userId: "52356929343539a89a52dc8d",
        goldProduct: {
            name: "200个金币",
            price: 20,
            priceUnit: "RMB",
            discount: -2,
            cost: 18,
            image: "images/gold/gold-200.png"
        },
        quantity: 1,
        discount: 0,
        totalCost: 18,
        orderDate: "Mon Nov 04 18:33:38 +0800 2013",
        status: "UNPAID"
    }
</tags:example>