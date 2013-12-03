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
            description: "贝壳，泛指软体动物的外壳。贝壳通常可以在海滩发现到，内里的生物通常已在冲上岸前消失。由于部份贝壳外形漂亮，有人会有收集贝壳的嗜好。",
            price: 3,
            imageUrl: "http://file.dev.lebooo.com/images/goods/shell.png",
            imageBiggerUrl: "http://file.dev.lebooo.com/images/goods/shell-bigger.png"
        },
        {
            id: 2,
            name: "玫瑰",
            description: "玫瑰（学名：Rosa rugosa）是蔷薇科蔷薇属植物，在日常生活中是蔷薇属一系列花大艳丽的栽培品种的统称。",
            price: 10,
            imageUrl: "http://file.dev.lebooo.com/images/goods/rose.png",
            imageBiggerUrl: "http://file.dev.lebooo.com/images/goods/rose-bigger.png"
        },
        {
            id: 3,
            name: "钻石",
            description: "钻石（英文：Diamond），化学中一般称为金刚石，钻石为经过琢磨的金刚石，金刚石则是指钻石的原石。",
            price: 100,
            imageUrl: "http://file.dev.lebooo.com/images/goods/diamond.png",
            imageBiggerUrl: "http://file.dev.lebooo.com/images/goods/diamond-bigger.png"
        }
    ]
</tags:example>