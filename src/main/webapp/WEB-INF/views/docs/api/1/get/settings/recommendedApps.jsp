<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="推荐应用" method="GET" action="${ctx}/api/1/settings/recommendedApps.json">
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/settings/recommendedApps.json">
    [
        {
            name: "乐播",
            url: "http://file.dev.lebooo.com/images/btn-dl-lebo-iphone.png",
            imageUrl: "http://file.lebooo.com/images/logo.png",
            backgroundColor: "#abcdef",
            version: "2.3",
            size: "20M"
        },
        {
            name: "乐播-6秒视频",
            url: "http://file.dev.lebooo.com/images/btn-dl-lebo-iphone.png",
            imageUrl: "http://file.lebooo.com/images/logo.png",
            backgroundColor: "#123456",
            version: "2.4",
            size: "21M"
        }
    ]
</tags:example>