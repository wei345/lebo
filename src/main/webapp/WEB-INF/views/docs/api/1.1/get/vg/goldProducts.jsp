<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="金币商品" method="GET" action="${ctx}/api/1.1/vg/goldProducts.json">

</tags:form>

<tags:example method="GET" url="http://192.168.1.103:8080/api/1.1/vg/goldProducts.json">
    [
        {
            name: "10个金币",
            description: "金币是一种全部或大部份由黄金制造的硬币。",
            price: 1,
            priceUnit: "CNY",
            discount: 0,
            cost: 1,
            imageUrl: "http://file.dev.lebooo.com/images/gold/gold-10.png"
        },
        {
            name: "50个金币",
            description: "黄金差不多在硬币发明之初，就因其价值被用来当作硬币。",
            price: 5,
            priceUnit: "CNY",
            discount: 0,
            cost: 5,
            imageUrl: "http://file.dev.lebooo.com/images/gold/gold-50.png"
        },
        {
            name: "200个金币",
            description: "黄金作为货币有很多理由。它的买卖价差低。",
            price: 20,
            priceUnit: "CNY",
            discount: -2,
            cost: 18,
            imageUrl: "http://file.dev.lebooo.com/images/gold/gold-200.png"
        },
        {
            name: "500个金币",
            description: "黄金可以分割成小单位，而不损害其价值；它也可以熔成锭，并且重铸成硬币。",
            price: 50,
            priceUnit: "CNY",
            discount: -10,
            cost: 40,
            imageUrl: "http://file.dev.lebooo.com/images/gold/gold-500.png"
        },
        {
            name: "1000个金币",
            description: "黄金的密度比大多数金属高，使假币很难流通。",
            price: 100,
            priceUnit: "CNY",
            discount: -30,
            cost: 70,
            imageUrl: "http://file.dev.lebooo.com/images/gold/gold-1000.png"
        }
    ]
</tags:example>