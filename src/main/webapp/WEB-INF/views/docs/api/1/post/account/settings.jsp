<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="更新用户设置" method="POST" action="${ctx}/api/1/account/settings.json">
    <p>
        更新APNS(Apple Push Notification Service) token。
    </p>
    <%--<p>
        APNS Development Token由于p12文件问题，暂不可用。
    </p>--%>
    <tags:field name="apnsDevelopmentToken" optional="true"/>
    <tags:field name="apnsProductionToken" optional="true"/>
    <p>
        帖子被回复时发送通知, <code>true</code>或<code>false</code>
    </p>
    <tags:field name="notifyOnReplyPost" optional="true"/>
    <p>
        帖子被喜欢时发送通知, <code>true</code>或<code>false</code>
    </p>
    <tags:field name="notifyOnFavorite" optional="true"/>
    <p>
        被关注时发送通知, <code>true</code>或<code>false</code>
    </p>
    <tags:field name="notifyOnFollow" optional="true"/>
    <p>
        收到通知时是否有提示音, <code>true</code>或<code>false</code>
    </p>
    <tags:field name="notifySound" optional="true"/>
    <p>
        收到通知时是否震动, <code>true</code>或<code>false</code>
    </p>
    <tags:field name="notifyVibrator" optional="true"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1/account/settings.json?notifySound=true">
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
