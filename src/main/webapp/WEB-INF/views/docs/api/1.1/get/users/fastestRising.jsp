<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="上升最快" method="GET" action="${ctx}/api/1.1/users/fastestRising.json">
    <tags:fields-page-size/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1.1/users/fastestRising.json">
    [
        {
            id: "521c1c161a88bff4d06f1988",
            screenName: "半妖的秋天",
            description: "一直以为自己会永远是那个在校园里无论男女都勾肩搭背，横冲直撞，笑声响彻整个校园的女子。。。。。。",
            profileImageUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-ms1lnn-1.jpg",
            profileImageBiggerUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-ms1lnn-1.jpg",
            profileImageOriginalUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-ms1lnn-1.jpg",
            createdAt: "Tue May 28 11:53:03 +0800 2013",
            followersCount: 0,
            friendsCount: 0,
            statusesCount: 6,
            beFavoritedCount: 6,
            viewCount: 28614,
            digestCount: 0,
            level: 0
        }
    ]
</tags:example>