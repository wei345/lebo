<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="用户设置" action="${ctx}/api/1/account/settings.json" method="GET">
    <p>返回当前登录用户的设置。</p>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/account/settings.json">
    {
        id: "5216d0dc1a8829c4ae1bbec3",
        screenName: "涛涛_IT",
        profileImageUrl: "http://file.dev.lebooo.com/5216d0dc1a8829c4ae1bbec4.png",
        profileImageBiggerUrl: "http://file.dev.lebooo.com/5216d0dd1a8829c4ae1bbec5.png",
        profileImageOriginalUrl: "http://file.dev.lebooo.com/5216d0de1a8829c4ae1bbec6.png",
        notifyOnReplyPost: false,
        notifyOnFavorite: false,
        notifyOnFollow: false,
        notifySound: true,
        apnsProductionToken: "",
        apnsDevelopmentToken: ""
    }
</tags:example>