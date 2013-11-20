<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="系统推荐的标签列表" method="GET" action="${ctx}/api/1/hashtags/suggestions.json">
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/hashtags/suggestions.json">
    [
        {
            name: "小编制作",
            title: "",
            description: "全是精品",
            imageUrl: "http://file.dev.lebooo.com/images/channels//xiao-bian-zhi-zuo-5609.png",
            backgroundColor: ""
        }
    ]
</tags:example>