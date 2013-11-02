<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="金币商品" method="GET" action="${ctx}/api/1.1/ec/goldProducts.json">

</tags:form>

<tags:example method="GET" url="http://192.168.1.103:8080/api/1.1/ec/goldProducts.json">
    [
        {
            productId: 1,
            name: "10个金币",
            price: 1,
            priceUnit: "RMB",
            discount: 0,
            image: "images/products/gold-10.png",
            cost: 1
        },
        {
            productId: 2,
            name: "50个金币",
            price: 5,
            priceUnit: "RMB",
            discount: 0,
            image: "images/products/gold-50.png",
            cost: 5
        },
        {
            productId: 3,
            name: "200个金币",
            price: 20,
            priceUnit: "RMB",
            discount: 0.1,
            image: "images/products/gold-200.png",
            cost: 18
        },
        {
            productId: 4,
            name: "500个金币",
            price: 50,
            priceUnit: "RMB",
            discount: 0.2,
            image: "images/products/gold-500.png",
            cost: 40
        },
        {
            productId: 5,
            name: "1000个金币",
            price: 100,
            priceUnit: "RMB",
            discount: 0.3,
            image: "images/products/gold-1000.png",
            cost: 70
        }
    ]
</tags:example>