<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="虚拟物品" method="GET" action="${ctx}/api/1.1/vg/goods.json">
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1.1/vg/goods.json">
    [
        {
            id: 1,
            name: "贝壳",
            price: 3,
            discount: 0,
            imageNormal: "images/goods/shell.png",
            imageBigger: "images/goods/shell-bigger.png"
        },
        {
            id: 2,
            name: "玫瑰",
            price: 10,
            discount: 0,
            imageNormal: "images/goods/rose.png",
            imageBigger: "images/goods/rose-bigger.png"
        },
        {
            id: 3,
            name: "钻石",
            price: 100,
            discount: 0,
            imageNormal: "images/goods/diamond.png",
            imageBigger: "images/goods/diamond-bigger.png"
        }
    ]
</tags:example>