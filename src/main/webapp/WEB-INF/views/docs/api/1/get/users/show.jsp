<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="查看用户" method="GET" action="${ctx}/api/1/users/show.json">
    <tags:field name="userId" value="51e6124ea0eedbd1aad37b6b"/>
    <tags:field name="sceenName" optional="xueeR_Z"/>
</tags:form>


<tags:example method="GET" url="http://localhost:8080/api/1/users/show.json?userId=51e6124ea0eedbd1aad37b6b">
    {
        id: "51def1e61a883914869e46f3",
        screenName: "法图_麦",
        name: "法图_麦",
        profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
        createdAt: 1373565414511,
        following: null,
        followersCount: 0,
        friendsCount: 1,
        statusesCount: 3,
        verified: null,
        location: null,
        timeZone: null
    }
</tags:example>