<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="获取用户profile" method="GET" action="${ctx}/api/1/im/profiles.json">
    <p>
        一个或多个用户ID，多个用户ID之间以<code>,</code>分隔。
    </p>
    <tags:field name="userIds"/>
</tags:form>


<tags:example method="GET" url="http://localhost:8080/api/1/im/profiles.json?userIds=521c1c161a88bff4d06f19a0%2C521c1c161a88bff4d06f19a2">
    [
        {
            id: "521c1c161a88bff4d06f19a0",
            screenName: "一比多胡再平",
            description: "18917000130.18938181491.021-28934162",
            profileImageUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-mqoask-.jpeg",
            profileImageBiggerUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-mqoask-.jpeg",
            profileImageOriginalUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-mqoask-.jpeg",
            createdAt: "Mon Jul 29 09:00:20 +0800 2013",
            followersCount: 0,
            friendsCount: 0,
            statusesCount: 0,
            beFavoritedCount: 0,
            viewCount: 0,
            digestCount: 0,
            level: 0
        },
        {
            id: "521c1c161a88bff4d06f19a2",
            screenName: "未知道幸福",
            description: "快乐不快乐都是自己的，所以要开心工作快乐赚钱。",
            profileImageUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-mnhhnu-.jpeg",
            profileImageBiggerUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-mnhhnu-.jpeg",
            profileImageOriginalUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-mnhhnu-.jpeg",
            createdAt: "Tue May 28 09:04:41 +0800 2013",
            followersCount: 0,
            friendsCount: 0,
            statusesCount: 0,
            beFavoritedCount: 0,
            viewCount: 88,
            digestCount: 0,
            level: 0
        }
    ]
</tags:example>