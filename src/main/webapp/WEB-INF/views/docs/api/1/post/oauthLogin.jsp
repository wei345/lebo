<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="OAuth登录" method="POST" action="${ctx}/api/1/oauthLogin.json">
    <p>
        provider: weibo, renren
    </p>
    <tags:field name="provider" value="weibo"/>
    <tags:field name="token" value="2.00vHLEwBz7QwTCbafc736d580QUCCY"/>
</tags:form>

<tags:example method="POST" url="/api/1/oauthLogin.json?provider=weibo&token=2.00vHLEwBz7QwTCbafc736d580QUCCY">
    {
        screenName: "涛涛_IT",
        apnsDevelopmentToken: "",
        profileImageUrl: "http://file.dev.lebooo.com/5216d0dc1a8829c4ae1bbec4.png",
        beFavoritedCount: 0,
        id: "5216d0dc1a8829c4ae1bbec3",
        notifyOnReplyPost: false,
        profileImageOriginalUrl: "http://file.dev.lebooo.com/5216d0de1a8829c4ae1bbec6.png",
        weiboVerified: false,
        notifyOnFavorite: false,
        level: 1,
        createdAt: "Fri Aug 23 11:02:52 +0800 2013",
        apnsProductionToken: "",
        profileImageBiggerUrl: "http://file.dev.lebooo.com/5216d0dd1a8829c4ae1bbec5.png",
        notifySound: true,
        notifyOnFollow: false,
        digestCount: 2,
        followersCount: 0,
        statusesCount: 3,
        viewCount: 0
    }
</tags:example>