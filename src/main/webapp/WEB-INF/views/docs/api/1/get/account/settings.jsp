<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="用户设置" action="${ctx}/api/1/account/settings.json" method="GET">
    <p>返回当前登录用户的设置。</p>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/account/settings.json">
    {
        id: "5211ebb90cf2bd294d22e3ff",
        screenName: "明丫丫是个爷们",
        description: "abc",
        profileImageUrl: "http://file.dev.lebooo.com/5211ebba0cf2bd294d22e401",
        profileImageBiggerUrl: "http://file.dev.lebooo.com/5211ebba0cf2bd294d22e401",
        profileImageOriginalUrl: "http://file.dev.lebooo.com/5211ebba0cf2bd294d22e402",
        notifyOnReplyPost: true,
        notifyOnFavorite: true,
        notifyOnFollow: true,
        notifySound: false,
        notifyVibrator: false,
        apnsProductionToken: "",
        apnsDevelopmentToken: ""
    }
</tags:example>