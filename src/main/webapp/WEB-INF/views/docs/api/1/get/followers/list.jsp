<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="Followers" action="${ctx}/api/1/followers/list.json" method="GET">
    <p>
        返回由userId或screenName指定的用户的粉丝，如果userId和screenName都未指定，则返回当前登录用户的粉丝。
    </p>
    <tags:field name="userId" value="51def53f1a883914869e46f5" optional="true"/>
    <tags:field name="screenName" value="法图_麦" optional="true"/>
    <tags:field name="page" value="" optional="true"/>
    <tags:field name="size" value="" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/followers/list.json?userId=51def53f1a883914869e46f5">
    [
        {
            id: "51e778ea1a8816dc79e40aaf",
            screenName: "liuwei",
            profileImageUrl: "/files/51ed11161a88f15acf2d87fd",
            createdAt: "Thu Jul 18 13:11:06 +0800 2013",
            following: false,
            followersCount: 0,
            friendsCount: 2,
            statusesCount: 5,
            favoritesCount: 3,
            beFavoritedCount: 1,
            viewsCount: 3,
            blocking: false
        }
    ]
</tags:example>