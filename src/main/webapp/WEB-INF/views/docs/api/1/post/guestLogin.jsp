<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="游客登录" method="POST" action="${ctx}/api/1/guestLogin.json">
    <p>没有参数</p>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1/guestLogin.json">
    {
        friendsCount: 1,
        screenName: "guest",
        apnsDevelopmentToken: "",
        beFavoritedCount: 0,
        id: "52b0574d1a8893eae397f866",
        notifyOnReplyPost: true,
        notifyOnFavorite: true,
        level: 0,
        createdAt: "Tue Dec 17 21:53:17 +0800 2013",
        notifyVibrator: false,
        apnsProductionToken: "",
        notifySound: false,
        notifyOnFollow: true,
        digestCount: 0,
        followersCount: 0,
        statusesCount: 0,
        viewCount: 0
    }
</tags:example>