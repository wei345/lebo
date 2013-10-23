<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="Top50" method="GET" action="${ctx}/api/1.1/users/top50.json">
    <tags:fields-page-size/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1.1/users/top50.json">
    [
        {
            id: "525124e81a88ac9dfcbd9ce0",
            screenName: "明丫丫是个爷们",
            profileImageUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce1",
            profileImageBiggerUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce3",
            profileImageOriginalUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce5",
            createdAt: "Sun Oct 06 16:52:56 +0800 2013",
            following: false,
            followersCount: 0,
            friendsCount: 0,
            statusesCount: 10,
            favoritesCount: 0,
            beFavoritedCount: 1,
            viewCount: 0,
            digestCount: 0,
            weiboVerified: false,
            blocking: false,
            bilateral: false,
            level: 0
        }
    ]
</tags:example>