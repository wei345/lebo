<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<p>
    把指定用户拉黑，如果这两个用户之间存在关注与被关注关系，也会被删除。
</p>
<tags:form name="拉黑" method="POST" action="${ctx}/api/1/blocks/create.json">
    <p>userId、screenName任选其一。</p>
    <tags:field name="screenName" value="法图_麦" optional="true"/>
    <tags:field name="userId" value="51def1e61a883914869e46f3" optional="true"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1/blocks/create.json?userId=51def1e61a883914869e46f3">
    {
        id: "51def1e61a883914869e46f3",
        screenName: "法图_麦",
        name: "法图_麦",
        profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
        createdAt: "Fri Jul 12 01:56:54 +0800 2013",
        following: false,
        followersCount: null,
        friendsCount: 1,
        statusesCount: 5,
        verified: null,
        location: null,
        timeZone: null,
        beFavoritedCount: null,
        viewsCount: 9
    }
</tags:example>