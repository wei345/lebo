<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="取消关注" method="POST" action="${ctx}/api/1/friendships/destroy.json">
    <p>userId或screenName。</p>
    <tags:field name="screenName" value="法图_麦" optional="true"/>
    <tags:field name="userId" value="51dbb3e21a887f15c8b6f042" optional="true"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1/friendships/create.json?userId=51dcf1d81a883e712783f124">
    {
        id: "51dcf1d81a883e712783f124",
        screenName: "法图_麦",
        name: "法图_麦",
        profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
        createdAt: 1373434328688,
        following: null,
        followersCount: null,
        friendsCount: null,
        statusesCount: null,
        verified: null,
        location: null,
        timeZone: null
    }
</tags:example>