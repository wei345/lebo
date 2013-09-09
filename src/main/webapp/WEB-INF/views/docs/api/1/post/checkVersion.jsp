<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="检查版本" method="POST" action="${ctx}/api/1/checkVersion.json">
    <p>
        <code>android</code>或<code>ios</code>
    </p>
    <tags:field name="os"/>
    <p>
        系统版本
    </p>
    <tags:field name="osVersion"/>
    <p>
        乐播版本
    </p>
    <tags:field name="version"/>
</tags:form>

<p>
    如果返回值中<code>forceUpgrade</code>为<code>true</code>，那么客户端要执行强制升级。
</p>

<tags:example method="POST" url="http://localhost:8080/api/1/checkVersion.json?os=ios&osVersion=6.1.3&version=1">
    {
        forceUpgrade: false,
        downloadUrl: "https://itunes.apple.com/cn/app/le-bo-6miao-shi-pin/id598266288?mt=8",
        version: "1.2.3",
        releaseAt: "Mon Sep 09 18:50:42 +0800 2013",
        releaseNotes: "test ios 6.1.3 1"
    }
</tags:example>