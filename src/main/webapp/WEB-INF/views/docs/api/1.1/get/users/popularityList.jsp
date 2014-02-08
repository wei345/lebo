<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="人气排行榜" method="GET" action="${ctx}/api/1.1/users/popularityList.json">
    <tags:fields-page-size/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1.1/users/popularityList.json">
    [
        {
            userId: "528b27921a888afc0a256db3",
            screenName: "烟雨醉相思",
            profileImageUrl: "http://file.dev.lebooo.com/user/2013-11-19/528b27921a888afc0a256db3-normal-6984.png",
            profileImageBiggerUrl: "http://file.dev.lebooo.com/user/2013-11-19/528b27921a888afc0a256db3-bigger-24561.png",
            profileImageOriginalUrl: "http://file.dev.lebooo.com/user/2013-11-19/528b27921a888afc0a256db3-original-24561.png",
            popularity: 20
        },
        {
            userId: "52356929343539a89a52dc8d",
            screenName: "admin",
            profileImageUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-normal-4252.png",
            profileImageBiggerUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-bigger-16865.png",
            profileImageOriginalUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-original-1705885.png",
            popularity: 0
        }
    ]
</tags:example>