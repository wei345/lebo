<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<p>
    频道列表
</p>
<tags:form name="选项" method="GET" action="${ctx}/api/1/settings/channels.json">
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/settings/channels.json">
    [
        {
            name: "番石榴",
            title: "guava",
            description: "番石榴为热带、亚热带水果，原产美洲，现在华南地区及四川盆地均有栽培，香甜可口。 成熟的番石榴为浅绿色，果皮脆薄，食用时一般不用削皮，果肉厚，清甜脆爽。其含有较丰富的蛋白质、维生素A、C等营养物质及磷、钙、镁等微量元素，是非常好的保健食品。",
            imageUrl: "/files/5200d2201a8840bd1ded2d2e",
            backgroundColor: "green"
        }
    ]
</tags:example>