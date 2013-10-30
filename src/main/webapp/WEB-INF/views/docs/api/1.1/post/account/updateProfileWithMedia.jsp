<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="更新Profile v1.1" method="POST" action="${ctx}/api/1.1/account/updateProfileWithMedia.json">
    <tags:field name="imageUrl" optional="true"/>
    <tags:field name="screenName" value="" optional="true"/>
    <tags:field name="description" value="介绍" optional="true"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1.1/account/updateProfileWithMedia.json">
    {
        id: "52356929343539a89a52dc8d",
        screenName: "admin",
        description: "秋",
        profileImageUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-normal-4252.png",
        profileImageBiggerUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-bigger-16865.png",
        profileImageOriginalUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-original-1705885.png",
        followersCount: 0,
        friendsCount: 0,
        statusesCount: 6,
        favoritesCount: 0,
        beFavoritedCount: 0,
        viewCount: 0,
        digestCount: 0,
        blocking: false,
        level: 0
    }
</tags:example>
