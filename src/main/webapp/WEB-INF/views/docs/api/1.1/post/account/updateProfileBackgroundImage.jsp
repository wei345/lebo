<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="修改用户个人页背景图片" method="POST" action="${ctx}/api/1.1/account/updateProfileBackgroundImage.json">
    <tags:field name="imageUrl"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1.1/account/updateProfileBackgroundImage.json?imageUrl=http%3A%2F%2Ffile.dev.lebooo.com%2Ftmp%2Fexpire-2013-11-19-21-58-36-image-png-528b607c1a8870c3d7830c5c.png">
    {
        id: "52356929343539a89a52dc8d",
        screenName: "admin",
        description: "秋",
        profileImageUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-normal-4252.png",
        profileImageBiggerUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-bigger-16865.png",
        profileImageOriginalUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-original-1705885.png",
        profileBackgroundImageUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-background-image-1009721.png",
        followersCount: 3,
        friendsCount: 0,
        statusesCount: 9,
        beFavoritedCount: 0,
        viewCount: 2,
        digestCount: 0,
        level: 0
    }
</tags:example>
