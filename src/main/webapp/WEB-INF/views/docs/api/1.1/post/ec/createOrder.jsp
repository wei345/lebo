<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="签名" method="POST" action="${ctx}/api/1.1/ec/createOrder.json">

    <tags:field name="productId"/>

</tags:form>

<tags:example method="GET" url="http://192.168.1.103:8080/api/1.1/ec/createOrder.json?productId=1">
    {
        orderId: 11,
        mongoUserId: "52356929343539a89a52dc8d",
        orderDate: "Fri Nov 01 17:37:34 +0800 2013",
        discount: 0,
        status: "UNPAID",
        orderDetails: [
            {
                product: {
                    productId: 1,
                    name: "10个金币",
                    price: 1,
                    priceUnit: "RMB",
                    discount: 0,
                    image: "images/products/gold-10.png"
                },
                quantity: 1,
                discount: 0
            }
        ]
    }
</tags:example>