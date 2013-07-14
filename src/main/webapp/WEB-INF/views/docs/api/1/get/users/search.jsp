<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="搜索用户" method="GET" action="${ctx}/api/1/users/search.json">
    <tags:field name="q" value="麦"/>
    <tags:field name="count" optional="true"/>
    <tags:field name="maxId" optional="true"/>
    <tags:field name="sinceId" optional="true"/>
</tags:form>


<tags:example method="GET" url="http://localhost:8080/api/1/users/search.json?q=%E9%BA%A6">
    [
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
    ]
</tags:example>