<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="双向关注好友" action="${ctx}/api/1/friends/bilateral.json" method="GET">
    <p>
        返回由userId或screenName指定的用户双向关注好友，如果userId和screenName都未指定，则返回当前登录用户的双向关注好友。
    </p>
    <tags:field name="userId" value="51def1e61a883914869e46f3" optional="true"/>
    <tags:field name="screenName" value="法图_麦" optional="true"/>
    <tags:field name="page" value="" optional="true"/>
    <tags:field name="size" value="" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/friends/bilateral.json?screenName=liuwei">
    [
        {
            id: "51def1e61a883914869e46f3",
            screenName: "法图_麦",
            profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
            createdAt: "Fri Jul 12 01:56:54 +0800 2013",
            followersCount: 1,
            friendsCount: 1,
            statusesCount: 9,
            favoritesCount: 3,
            beFavoritedCount: 1,
            blocking: false
        }
    ]
</tags:example>